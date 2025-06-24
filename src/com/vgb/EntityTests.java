package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JUnit test suite for VGB invoice system.
 */
public class EntityTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of a piece of equipment and tests if its cost and tax
	 * calculations are correct.
	 *
	 */
	@Test
	public void testEquipment() {

		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;

		Equipment e = new Equipment(uuid, name, model, cost);

		double expectedCost = 95125.00;
		double expectedTax = 4994.06;

		double actualCost = e.getCost();
		double actualTax = e.getTaxes();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);

		String s = e.toString();

		assertTrue(s.contains("Backhoe 3000"));
		assertTrue(s.contains("BH30X2"));
		assertTrue(s.contains("95125.00"));
		assertTrue(s.contains("4994.06"));

	}

	/**
	 * Creates an instance of a piece of equipment that is leased and tests if its
	 * cost and tax calculations are correct.
	 * 
	 */
	@Test
	public void testLease() {

		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;

		LocalDate leaseStart = LocalDate.parse("2024-01-01");
		LocalDate leaseEnd = LocalDate.parse("2026-06-01");

		LeasedEquipment le = new LeasedEquipment(uuid, name, model, cost, leaseStart, leaseEnd);

		double expectedCost = 69037.29;
		double expectedTax = 1500;

		double actualCost = le.getCost();
		double actualTax = le.getTaxes();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);

		String s = le.toString();

		assertTrue(s.contains("Backhoe 3000"));
		assertTrue(s.contains("BH30X2"));
		assertTrue(s.contains("69037.29"));
		assertTrue(s.contains("1500"));
		assertTrue(s.contains("2024-01-01"));
		assertTrue(s.contains("2026-06-01"));
	}

	/**
	 * Creates an instance of a piece of equipment that is rented and tests if its
	 * cost and tax calculations are correct.
	 * 
	 */
	@Test
	public void testRental() {

		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;
		double hoursRented = 25;

		RentedEquipment re = new RentedEquipment(uuid, name, model, cost, hoursRented);

		double expectedCost = 2378.13;
		double expectedTax = 104.16;

		double actualCost = re.getCost();
		double actualTax = re.getTaxes();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);

		String s = re.toString();

		assertTrue(s.contains("Backhoe 3000"));
		assertTrue(s.contains("BH30X2"));
		assertTrue(s.contains("2378.13"));
		assertTrue(s.contains("104.16"));
		assertTrue(s.contains("25"));

	}

	/**
	 * Creates an instance of a material and tests if its cost and tax calculations
	 * are correct.
	 * 
	 */
	@Test
	public void testMaterial() {

		UUID uuid = UUID.randomUUID();
		String name = "Nails";
		String unit = "Box";
		double costPerUnit = 9.99;
		double numUnits = 31;

		Material m = new Material(uuid, name, unit, costPerUnit, numUnits);

		double expectedCost = 309.69;
		double expectedTax = 22.14;

		double actualCost = m.getCost();
		double actualTax = m.getTaxes();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);

		String s = m.toString();

		assertTrue(s.contains("Nails"));
		assertTrue(s.contains("Box"));
		assertTrue(s.contains("309.69"));
		assertTrue(s.contains("22.14"));
		assertTrue(s.contains("9.99"));
		assertTrue(s.contains("31"));
	}

	/**
	 * Creates an instance of a contract and tests if its cost and tax calculations
	 * are correct.
	 * 
	 */
	@Test
	public void testContract() {

		UUID uuid = UUID.randomUUID();
		String name = "foundation pour";
		double amount = 10500;

		Contract c = new Contract(uuid, name, null, amount);

		double expectedCost = 10500;
		double expectedTax = 0;

		double actualCost = c.getCost();
		double actualTax = c.getTaxes();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);

		String s = c.toString();
		
		assertTrue(s.contains("foundation pour"));
		assertTrue(s.contains("10500"));

	}

}