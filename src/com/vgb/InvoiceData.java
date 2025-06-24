package com.vgb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Aden Smith 2025-04-30
 * 
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Helper method that closes the given connection and prepared statement if open
	 * 
	 * @param conn
	 * @param ps
	 */
	public static void closeResources(Connection conn, PreparedStatement ps) {
		try {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper method that closes the given connection, prepared statement, and
	 * result set if open
	 * 
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper function that returns the personId corresponding to the given
	 * personUuid
	 * 
	 * @param personUuid
	 * @param conn
	 * @param ps
	 * @param rs
	 * @return
	 */
	public static int getPersonId(UUID personUuid, Connection conn, PreparedStatement ps, ResultSet rs) {

		String query = "select personId from Person where personUuid = ?;";
		int personId = 0;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personUuid.toString());
			rs = ps.executeQuery();

			if (rs.next()) {
				personId = rs.getInt(1);
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return personId;
	}

	/**
	 * Helper function that returns the itemId corresponding to the given itemUuid
	 * 
	 * @param itemUuid
	 * @param conn
	 * @param ps
	 * @param rs
	 * @return
	 */
	public static int getItemId(UUID itemUuid, Connection conn, PreparedStatement ps, ResultSet rs) {

		int itemId = 0;

		String query = "select itemId from Item where itemUuid = ?";

		try {

			ps = conn.prepareStatement(query);
			ps.setString(1, itemUuid.toString());

			rs = ps.executeQuery();

			if (rs.next()) {
				itemId = rs.getInt(1);
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return itemId;
	}

	/**
	 * Helper function that returns the invoiceId corresponding to the given
	 * invoiceUuid
	 * 
	 * @param invoiceUuid
	 * @param conn
	 * @param ps
	 * @param rs
	 * @return
	 */
	public static int getInvoiceId(UUID invoiceUuid, Connection conn, PreparedStatement ps, ResultSet rs) {
		int invoiceId = 0;

		String query = "select invoiceId from Invoice where invoiceUuid = ?";

		try {

			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceUuid.toString());

			rs = ps.executeQuery();

			if (rs.next()) {
				invoiceId = rs.getInt(1);
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return invoiceId;
	}

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {

		Connection conn = DatabaseConnection.getConnection();

		PreparedStatement ps = null;

		try {
			String query = "delete from InvoiceItem;";

			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "delete from Item;";

			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "delete from Invoice;";

			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "delete from Company;";

			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "delete from Address;";

			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "delete from Email;";

			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "delete from Person;";

			ps = conn.prepareStatement(query);
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps);

	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param phone
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {

		Connection conn = DatabaseConnection.getConnection();

		String query = "insert into Person (personUuid, firstName, lastName, phone) values\n" + "  (?, ?, ?, ?);";

		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);

			ps.setString(1, personUuid.toString());
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, phone);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps);

		return;
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {

		Connection conn = DatabaseConnection.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		int personId = getPersonId(personUuid, conn, ps, rs);

		String query = "insert into Email (email, personId) values (?, ?);";

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setInt(2, personId);
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);

		return;

	}

	/**
	 * Adds a company record to the database with the primary contact person
	 * identified by the given code.
	 *
	 * @param companyUuid
	 * @param name
	 * @param contactUuid
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city,
			String state, String zip) {

		Connection conn = DatabaseConnection.getConnection();

		String query = "insert into Address (street, city, zipCode, stateAbbreviation) values (?, ?, ?, ?);";
		int addressId = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setString(4, state);
			ps.executeUpdate();

			ResultSet keys = ps.getGeneratedKeys();

			keys.next();
			addressId = keys.getInt(1);

			keys.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		int personId = getPersonId(contactUuid, conn, ps, rs);

		query = "insert into Company (companyUuid, companyName, contactPersonId, addressId) values (?, ?, ?, ?);";

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, companyUuid.toString());
			ps.setString(2, name);
			ps.setInt(3, personId);
			ps.setInt(4, addressId);

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);
	}

	/**
	 * Adds an equipment record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param modelNumber
	 * @param retailPrice
	 */
	public static void addEquipment(UUID equipmentUuid, String name, String modelNumber, double retailPrice) {

		Connection conn = DatabaseConnection.getConnection();

		String query = "insert into Item (itemUuid, itemType, itemName, modelNumber, retailPrice) values (?, ?, ?, ?, ?);";

		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, equipmentUuid.toString());
			ps.setString(2, "E");
			ps.setString(3, name);
			ps.setString(4, modelNumber);
			ps.setDouble(5, retailPrice);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps);
	}

	/**
	 * Adds an material record to the database of the given values.
	 *
	 * @param materialUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addMaterial(UUID materialUuid, String name, String unit, double pricePerUnit) {
		Connection conn = DatabaseConnection.getConnection();

		String query = "insert into Item (itemUuid, itemType, itemName, unit, costPerUnit) values (?, ?, ?, ?, ?);";

		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, materialUuid.toString());
			ps.setString(2, "M");
			ps.setString(3, name);
			ps.setString(4, unit);
			ps.setDouble(5, pricePerUnit);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps);
	}

	/**
	 * Adds an contract record to the database of the given values.
	 *
	 * @param contractUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addContract(UUID contractUuid, String name, UUID servicerUuid) {
		Connection conn = DatabaseConnection.getConnection();

		int companyId = 0;

		String query = "select companyId from Company where companyUuid = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, servicerUuid.toString());
			rs = ps.executeQuery();

			if (rs.next()) {
				companyId = rs.getInt(1);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		query = "insert into Item (itemUuid, itemType, itemName, servicer) values (?, ?, ?, ?);";

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, contractUuid.toString());
			ps.setString(2, "C");
			ps.setString(3, name);
			ps.setInt(4, companyId);

			ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		closeResources(conn, ps, rs);
	}

	/**
	 * Adds an Invoice record to the database with the given data.
	 *
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		int personId = getPersonId(salesPersonUuid, conn, ps, rs);

		int companyId = 0;

		String query = "select companyId from Company where companyUuid = ?";

		try {

			ps = conn.prepareStatement(query);
			ps.setString(1, customerUuid.toString());

			rs = ps.executeQuery();

			if (rs.next()) {
				companyId = rs.getInt(1);
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		query = "insert into Invoice (invoiceUuid, date, salesPersonId, companyId) values (?, ?, ?, ?);";

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceUuid.toString());
			ps.setString(2, date.toString());
			ps.setInt(3, personId);
			ps.setInt(4, companyId);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);

	}

	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid) {

		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId = getItemId(itemUuid, conn, ps, rs);

		int invoiceId = getInvoiceId(invoiceUuid, conn, ps, rs);

		String query = "insert into InvoiceItem (invoiceId, itemId, equipmentType) values (?, ?, ?)";

		try {

			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setString(3, "P");

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);

	}

	/**
	 * Adds an Equipment lease record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param start
	 * @param end
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate start, LocalDate end) {

		Connection conn = DatabaseConnection.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId = getItemId(itemUuid, conn, ps, rs);

		int invoiceId = getInvoiceId(invoiceUuid, conn, ps, rs);

		String query = "insert into InvoiceItem (invoiceId, itemId, equipmentType, leaseStart, leaseEnd) values (?, ?, ?, ?, ?)";

		try {

			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setString(3, "L");
			ps.setString(4, start.toString());
			ps.setString(5, end.toString());

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);

	}

	/**
	 * Adds an Equipment rental record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfHours
	 */
	public static void addEquipmentRentalToInvoice(UUID invoiceUuid, UUID itemUuid, double numberOfHours) {

		Connection conn = DatabaseConnection.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId = getItemId(itemUuid, conn, ps, rs);

		int invoiceId = getInvoiceId(invoiceUuid, conn, ps, rs);

		String query = "insert into InvoiceItem (invoiceId, itemId, equipmentType, hoursRented) values (?, ?, ?, ?)";

		try {

			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setString(3, "R");
			ps.setDouble(4, numberOfHours);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);
	}

	/**
	 * Adds a material record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addMaterialToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId = getItemId(itemUuid, conn, ps, rs);

		int invoiceId = getInvoiceId(invoiceUuid, conn, ps, rs);

		String query = "insert into InvoiceItem (invoiceId, itemId, numUnits) values (?, ?, ?)";

		try {

			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, numberOfUnits);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);

	}

	/**
	 * Adds a contract record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param amount
	 */
	public static void addContractToInvoice(UUID invoiceUuid, UUID itemUuid, double amount) {
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		int itemId = getItemId(itemUuid, conn, ps, rs);

		int invoiceId = getInvoiceId(invoiceUuid, conn, ps, rs);

		String query = "insert into InvoiceItem (invoiceId, itemId, amount) values (?, ?, ?)";

		try {

			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, amount);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		closeResources(conn, ps, rs);

	}

}