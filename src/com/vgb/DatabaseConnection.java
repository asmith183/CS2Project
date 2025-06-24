package com.vgb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Aden Smith 2025-04-16
 *  Connection factory for the database
 */
public class DatabaseConnection {

	/**
	 * User name used to connect to the SQL server
	 */
	public static final String USERNAME = "asmith183";

	/**
	 * Password used to connect to the SQL server
	 */
	public static final String PASSWORD = "aeGaeneeh9va";

	/**
	 * Connection parameters that may be necessary for server configuration
	 * 
	 */
	public static final String PARAMETERS = "";

	/**
	 * SQL server to connect to
	 */
	public static final String SERVER = "cse-linux-01.unl.edu";

	/**
	 * Fully formatted URL for a JDBC connection
	 */
	public static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);

	/**
	 * Creates a connection to the server
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("Connection failed");
		}
	}
}
