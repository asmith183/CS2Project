package com.vgb;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Loads the CSV data files and creates lists of Persons, Companies, and Items
 */
public class DataLoader {

	/**
	 * Takes a UUID of a person and a list of persons and returns the person
	 * matching the UUID
	 * 
	 * @param uuid
	 * @param listPersons
	 * @return
	 */
	public static Person findPerson(UUID uuid, List<Person> listPersons) {
		for (Person p : listPersons) {
			if (uuid.equals(p.getUuid())) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Takes a company UUID and a list of companies and returns the company matching
	 * the company UUID
	 * 
	 * @param uuid
	 * @param companyList
	 * @return
	 */
	public static Company findCompany(UUID uuid, List<Company> companyList) {
		for (Company c : companyList) {
			if (uuid.equals(c.getCompanyUUID())) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Loads Person data from the given CSV file
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<Person> loadPersonData(String filePath) {

		List<Person> data = new ArrayList<>();

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
			data.add(p);
		}

		return data;
	}

	/**
	 * Loads Company data from the given CSV file
	 * 
	 * @param filePath
	 * @param personList
	 * @return
	 */
	public static List<Company> loadCompanyData(String filePath, List<Person> personList) {

		List<Company> data = new ArrayList<>();

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
				Person p = findPerson(contactUUID, personList);
				Company c = new Company(uuid, p, name, a);
				data.add(c);

			}
		}

		return data;
	}

	/**
	 * Loads item data from the given CSV file
	 * 
	 * @param filePath
	 * @param companyList
	 * @return
	 */
	public static List<Item> loadItemData(String filePath, List<Company> companyList) {
		List<Item> data = new ArrayList<>();

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

				data.add(e);

			} else if (type == 'M') {
				String unit = tokens[3];
				double costPerUnit = Double.parseDouble(tokens[4]);

				Material m = new Material(uuid, name, unit, costPerUnit);

				data.add(m);
			} else {
				UUID companyUUID = UUID.fromString(tokens[3]);
				Company company = findCompany(companyUUID, companyList);
				Contract c = new Contract(uuid, name, company);

				data.add(c);
			}

		}

		return data;
	}

}
