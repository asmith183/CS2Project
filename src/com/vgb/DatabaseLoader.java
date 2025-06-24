package com.vgb;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 * Aden Smith 2025-04-16
 * 
 * Provides methods for loading invoice data from the database
 */
public class DatabaseLoader {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);

	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
		LOGGER.info("Started...");
	}

	/**
	 * Loads person data from the database
	 * 
	 * @return
	 */
	public static Map<UUID, Person> loadPersonData() {

		LOGGER.debug("loading person data...");

		Map<UUID, Person> personMap = new HashMap<>();

		Connection conn = DatabaseConnection.getConnection();

		String query = "select personUuid as UUID, firstName, lastName, phone from Person;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String phone = rs.getString("phone");

				Person p = new Person(uuid, firstName, lastName, phone);

				personMap.put(uuid, p);
			}
		} catch (SQLException e) {
			LOGGER.error("ERROR: query failed", e);
			throw new RuntimeException(e);
		}

		query = "select p.personUuid as UUID, e.email\n" + "  from Person p\n"
				+ "  left join Email e on p.personId = e.personId\n" + "  order by p.personId;";

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String emailString = rs.getString("email");

				personMap.get(uuid).addEmail(emailString);
			}
		} catch (SQLException e) {
			LOGGER.error("ERROR: query failed", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("ERROR: resource closure failed", e);
			throw new RuntimeException(e);
		}

		LOGGER.info("successfully loaded person data...");

		return personMap;
	}

	/**
	 * Loads company data from the database
	 * 
	 * @return
	 */
	public static Map<UUID, Company> loadCompanyData() {

		LOGGER.debug("loading company data...");

		Map<UUID, Company> companyMap = new HashMap<>();
		Map<UUID, Person> personMap = loadPersonData();

		Connection conn = DatabaseConnection.getConnection();

		String query = "  select c.companyUuid as uuid, c.companyName as name, a.street, a.city, a.zipCode, a.stateAbbreviation, p.personUuid\n"
				+ "    from Company c\n" + "	join Address a on c.addressId = a.addressId\n"
				+ "    join Person p on c.contactPersonId = p.personId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String street = rs.getString("street");
				String city = rs.getString("city");
				String zip = rs.getString("zipCode");
				String state = rs.getString("stateAbbreviation");

				Address a = new Address(street, city, state, zip);

				UUID contactUuid = UUID.fromString(rs.getString("personUuid"));
				Person p = personMap.get(contactUuid);

				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String name = rs.getString("name");

				Company c = new Company(uuid, p, name, a);

				companyMap.put(uuid, c);

			}

		} catch (SQLException e) {
			LOGGER.error("ERROR: query failed", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("ERROR: resource closure failed", e);
			throw new RuntimeException(e);
		}

		LOGGER.info("successfully loaded company data...");

		return companyMap;
	}

	/**
	 * Loads item data from the database
	 * 
	 * @return
	 */
	public static Map<UUID, Item> loadItemData() {

		LOGGER.debug("loading item data...");

		Map<UUID, Item> itemMap = new HashMap<>();
		Map<UUID, Company> companyMap = loadCompanyData();

		Connection conn = DatabaseConnection.getConnection();

		String query = "select i.itemId, i.itemUuid, i.itemName, i.itemType, i.modelNumber, i.retailPrice, i.unit, i.costPerUnit, c.companyUuid\n"
				+ "  from Item i\n" + "  left join Company c on i.servicer = c.companyId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("itemUuid"));
				String type = rs.getString("itemType");
				String name = rs.getString("itemName");

				if (type.equals("E")) {
					String modelNumber = rs.getString("modelNumber");
					double retailPrice = rs.getDouble("retailPrice");

					Equipment e = new Equipment(uuid, name, modelNumber, retailPrice);

					itemMap.put(uuid, e);
				} else if (type.equals("M")) {
					String unit = rs.getString("unit");
					double costPerUnit = rs.getDouble("costPerUnit");

					Material m = new Material(uuid, name, unit, costPerUnit);

					itemMap.put(uuid, m);
				} else {
					UUID companyUuid = UUID.fromString(rs.getString("companyUuid"));
					Company c = companyMap.get(companyUuid);
					Contract contract = new Contract(uuid, name, c);

					itemMap.put(uuid, contract);
				}

			}

		} catch (SQLException e) {
			LOGGER.error("ERROR: query failed", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("ERROR: resource closure failed", e);
			throw new RuntimeException(e);
		}

		LOGGER.info("successfully loaded item data...");

		return itemMap;
	}

	/**
	 * Loads invoice item data from the database
	 * 
	 * @return
	 */
	public static Map<UUID, List<Billable>> loadInvoiceItems() {

		LOGGER.debug("loading invoice items...");

		Map<UUID, List<Billable>> invoiceItems = new HashMap<>();
		Map<UUID, Item> itemMap = loadItemData();

		Connection conn = DatabaseConnection.getConnection();

		String query = "  select ii.equipmentType, ii.hoursRented, ii.leaseStart, ii.leaseEnd, ii.numUnits, ii.amount, i.invoiceUuid, it.itemUuid\n"
				+ "    from InvoiceItem ii\n" + "    left join Invoice i on ii.invoiceId = i.invoiceId\n"
				+ "    left join Item it on ii.itemId = it.itemId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				UUID invoiceUuid = UUID.fromString(rs.getString("invoiceUuid"));
				UUID itemUuid = UUID.fromString(rs.getString("itemUuid"));

				if (!invoiceItems.containsKey(invoiceUuid)) {
					List<Billable> items = new ArrayList<>();
					invoiceItems.put(invoiceUuid, items);
				}

				if (itemMap.get(itemUuid) instanceof Equipment) {
					String type = rs.getString("equipmentType");

					if (type.equals("P")) {
						Equipment e = (Equipment) itemMap.get(itemUuid);
						invoiceItems.get(invoiceUuid).add(e);
					} else if (type.equals("R")) {
						Equipment e = (Equipment) itemMap.get(itemUuid);
						double hoursRented = rs.getDouble("hoursRented");
						RentedEquipment re = new RentedEquipment(e, hoursRented);
						invoiceItems.get(invoiceUuid).add(re);
					} else if (type.equals("L")) {
						Equipment e = (Equipment) itemMap.get(itemUuid);
						LocalDate leaseStart = LocalDate.parse(rs.getString("leaseStart"));
						LocalDate leaseEnd = LocalDate.parse(rs.getString("leaseEnd"));
						LeasedEquipment le = new LeasedEquipment(e, leaseStart, leaseEnd);
						invoiceItems.get(invoiceUuid).add(le);
					}

				} else if (itemMap.get(itemUuid) instanceof Material) {
					Material m = (Material) itemMap.get(itemUuid);
					double numUnits = rs.getDouble("numUnits");
					Material material = new Material(m, numUnits);
					invoiceItems.get(invoiceUuid).add(material);

				} else {
					Contract c = (Contract) itemMap.get(itemUuid);
					double amount = rs.getDouble("amount");
					Contract contract = new Contract(c, amount);
					invoiceItems.get(invoiceUuid).add(contract);

				}

			}

		} catch (SQLException e) {
			LOGGER.error("ERROR: query failed", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("ERROR: resource closure failed", e);
			throw new RuntimeException(e);
		}

		LOGGER.info("successfully loaded invoice items...");

		return invoiceItems;
	}

	/**
	 * Loads invoices from the database
	 * 
	 * @return
	 */
	public static SortedLinkedList<Invoice> loadInvoices(Comparator<Invoice> cmp) {

		LOGGER.debug("loading invoices...");

		// List<Invoice> list = new ArrayList<>();
		SortedLinkedList<Invoice> invoiceList = new SortedLinkedList<>(cmp);

		Map<UUID, Person> personMap = loadPersonData();
		Map<UUID, Company> companyMap = loadCompanyData();
		Map<UUID, List<Billable>> invoiceItems = loadInvoiceItems();

		Connection conn = DatabaseConnection.getConnection();

		String query = "select i.invoiceUuid, i.date, p.personUuid, c.companyUuid\n" + "  from Invoice i\n"
				+ "  left join Person p on i.salesPersonId = p.personId\n"
				+ "  left join Company c on i.companyId = c.companyId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("invoiceUuid"));
				UUID customerUuid = UUID.fromString(rs.getString("companyUuid"));
				UUID personUuid = UUID.fromString(rs.getString("personUuid"));
				Company c = companyMap.get(customerUuid);
				Person p = personMap.get(personUuid);
				LocalDate date = LocalDate.parse(rs.getString("date"));
				List<Billable> items = invoiceItems.get(uuid);

				Invoice i = new Invoice(uuid, date, c, p, items);

				invoiceList.add(i);
			}

		} catch (SQLException e) {
			LOGGER.error("ERROR: query failed", e);
			throw new RuntimeException(e);
		}

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LOGGER.error("ERROR: resource closure failed", e);
			throw new RuntimeException(e);
		}

		LOGGER.info("successfully loaded invoices...");

		return invoiceList;
	}

}