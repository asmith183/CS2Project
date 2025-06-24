package com.vgb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Provides methods to load invoice CSV data
 */
public class InvoiceLoader {

	/**
	 * Loads Person data from the given CSV file
	 * 
	 * @param filePath
	 * @return
	 */
	public static Map<UUID, Person> loadPersonData(String filePath) {

		Map<UUID, Person> personMap = new HashMap<>();

		Path path = Paths.get(filePath);

		List<String> contents;

		try {
			contents = Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		contents.remove(0);

		for (String line : contents) {

			if (line.trim().isEmpty()) {
				continue;
			}

			String tokens[] = line.split(",");

			UUID uuid = UUID.fromString(tokens[0]);
			String firstName = tokens[1];
			String lastName = tokens[2];
			String phone = tokens[3];

			List<String> emailList = new ArrayList<>();

			for (int i = 4; i < tokens.length; i++) {
				emailList.add(tokens[i]);
			}

			Person p = new Person(uuid, firstName, lastName, phone, emailList);
			personMap.put(uuid, p);
		}

		return personMap;
	}

	/**
	 * Loads company data from the given CSV file
	 * 
	 * @param filePath
	 * @param personMap
	 * @return
	 */
	public static Map<UUID, Company> loadCompanyData(String filePath, Map<UUID, Person> personMap) {

		Map<UUID, Company> companyMap = new HashMap<>();

		Path path = Paths.get(filePath);

		List<String> contents;

		try {
			contents = Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		contents.remove(0);

		for (String line : contents) {
			String tokens[] = line.split(",", 7);
			if (tokens.length == 7) {
				UUID uuid = UUID.fromString(tokens[0]);
				UUID contactUUID = UUID.fromString(tokens[1]);
				String name = tokens[2];
				String street = tokens[3];
				String city = tokens[4];
				String state = tokens[5];
				String zip = tokens[6];

				Address a = new Address(street, city, state, zip);
				Person p = personMap.get(contactUUID);
				Company c = new Company(uuid, p, name, a);

				companyMap.put(uuid, c);
			}
		}

		return companyMap;
	}

	/**
	 * Loads item data from the given CSV file
	 * 
	 * @param filePath
	 * @param companyList
	 * @return
	 */
	public static Map<UUID, Item> loadItemData(String filePath, Map<UUID, Company> companyMap) {
		Map<UUID, Item> itemMap = new HashMap<>();

		Path path = Paths.get(filePath);

		List<String> contents;

		try {
			contents = Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		contents.remove(0);

		for (String line : contents) {
			String tokens[] = line.split(",");

			UUID uuid = UUID.fromString(tokens[0]);
			char type = tokens[1].charAt(0);
			String name = tokens[2];

			if (type == 'E') {
				String modelNumber = tokens[3];
				double retailPrice = Double.parseDouble(tokens[4]);

				Equipment e = new Equipment(uuid, name, modelNumber, retailPrice);

				itemMap.put(uuid, e);

			} else if (type == 'M') {
				String unit = tokens[3];
				double costPerUnit = Double.parseDouble(tokens[4]);

				Material m = new Material(uuid, name, unit, costPerUnit);

				itemMap.put(uuid, m);
			} else {
				UUID companyUUID = UUID.fromString(tokens[3]);
				Company company = companyMap.get(companyUUID);
				Contract c = new Contract(uuid, name, company);

				itemMap.put(uuid, c);
			}

		}

		return itemMap;
	}

	/**
	 * Loads invoice item data from the given CSV file
	 * 
	 * @param filePath
	 * @param itemMap
	 * @return
	 */
	public static Map<UUID, List<Billable>> loadInvoiceItems(String filePath, Map<UUID, Item> itemMap) {
		Map<UUID, List<Billable>> invoiceItems = new HashMap<>();

		Path path = Paths.get(filePath);

		List<String> contents;

		try {
			contents = Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		contents.remove(0);

		for (String line : contents) {
			String tokens[] = line.split(",");

			UUID invoiceUuid = UUID.fromString(tokens[0]);
			UUID itemUuid = UUID.fromString(tokens[1]);

			if (!invoiceItems.containsKey(invoiceUuid)) {
				List<Billable> items = new ArrayList<>();
				invoiceItems.put(invoiceUuid, items);
			}

			if (tokens[2].charAt(0) == 'P') {
				Equipment e = (Equipment) itemMap.get(itemUuid);
				invoiceItems.get(invoiceUuid).add(e);
			} else if (tokens[2].charAt(0) == 'R') {
				Equipment e = (Equipment) itemMap.get(itemUuid);
				double hoursRented = Double.parseDouble(tokens[3]);
				RentedEquipment re = new RentedEquipment(e, hoursRented);
				invoiceItems.get(invoiceUuid).add(re);
			} else if (tokens[2].charAt(0) == 'L') {
				Equipment e = (Equipment) itemMap.get(itemUuid);
				LocalDate startDate = LocalDate.parse(tokens[3]);
				LocalDate endDate = LocalDate.parse(tokens[4]);
				LeasedEquipment le = new LeasedEquipment(e, startDate, endDate);
				invoiceItems.get(invoiceUuid).add(le);
			} else if (itemMap.get(UUID.fromString(tokens[1])) instanceof Material) {
				Material m = (Material) itemMap.get(itemUuid);
				double numUnits = Double.parseDouble(tokens[2]);
				Material material = new Material(m, numUnits);
				invoiceItems.get(invoiceUuid).add(material);
			} else {
				Contract c = (Contract) itemMap.get(itemUuid);
				double amount = Double.parseDouble(tokens[2]);
				Contract contract = new Contract(c, amount);
				invoiceItems.get(invoiceUuid).add(contract);
			}
		}

		return invoiceItems;
	}

	/**
	 * Loads invoices from the given CSV file
	 * 
	 * @param filePath
	 * @param companyMap
	 * @param personMap
	 * @param itemMap
	 * @return
	 */
	public static List<Invoice> loadInvoices(String filePath, Map<UUID, Company> companyMap,
			Map<UUID, Person> personMap, Map<UUID, List<Billable>> itemMap) {

		List<Invoice> list = new ArrayList<>();

		Path path = Paths.get(filePath);

		List<String> contents;

		try {
			contents = Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		contents.remove(0);

		for (String line : contents) {
			if (line.trim().isEmpty()) {
				continue;
			}

			String tokens[] = line.split(",");

			UUID uuid = UUID.fromString(tokens[0]);
			UUID customerUuid = UUID.fromString(tokens[1]);
			UUID personUuid = UUID.fromString(tokens[2]);
			Company customer = companyMap.get(customerUuid);
			Person salesPerson = personMap.get(personUuid);
			LocalDate date = LocalDate.parse(tokens[3]);
			List<Billable> items = itemMap.get(uuid);

			Invoice i = new Invoice(uuid, date, customer, salesPerson, items);

			list.add(i);

		}

		return list;

	}

}
