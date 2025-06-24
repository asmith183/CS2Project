package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for VGB invoice system.
 */
public class InvoiceTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in the VGB
	 * system.
	 */
	@Test
	public void testInvoice01() {

		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;

		Equipment e = new Equipment(uuid, name, model, cost);

		uuid = UUID.randomUUID();
		name = "foundation pour";
		double amount = 10500;

		Contract c = new Contract(uuid, name, null, amount);

		uuid = UUID.randomUUID();
		name = "Nails";
		String unit = "Box";
		double costPerUnit = 9.99;
		double numUnits = 31;

		Material m = new Material(uuid, name, unit, costPerUnit, numUnits);

		List<Billable> list = new ArrayList<>();
		list.add(e);
		list.add(c);
		list.add(m);

		uuid = UUID.randomUUID();
		String first = "John";
		String last = "Smith";
		String email1 = "jsmith1@gmail.com";
		String email2 = "john@hotmail.com";
		List<String> listEmails = new ArrayList<>();
		listEmails.add(email1);
		listEmails.add(email2);
		Person john = new Person(uuid, first, last, null, listEmails);

		uuid = UUID.randomUUID();
		String jasonFirst = "Jason";
		String jasonLast = "Smith";
		String jemail1 = "jasonsmith1@gmail.com";
		String jemail2 = "jason@hotmail.com";
		List<String> jasonEmails = new ArrayList<>();
		jasonEmails.add(jemail1);
		jasonEmails.add(jemail2);
		Person jason = new Person(uuid, jasonFirst, jasonLast, null, jasonEmails);

		UUID companyUUID = UUID.randomUUID();
		Person contactPerson = jason;
		String compName = "The Best Company";

		String street = "1034 West Oak Rd";
		String city = "Lincoln";
		String state = "NE";
		String zip = "90029";

		Address a = new Address(street, city, state, zip);

		Company company = new Company(companyUUID, contactPerson, compName, a);

		uuid = UUID.randomUUID();
		LocalDate date = LocalDate.of(2024, 12, 25);

		Invoice i = new Invoice(uuid, date, company, john, list);

		String s = i.toString();

		double expectedSubtotal = 105934.69;
		double expectedTaxTotal = 5016.2;
		double expectedGrandTotal = 110950.89;

		double actualSubtotal = i.getSubTotal();
		double actualTaxTotal = i.getTaxTotal();
		double actualGrandTotal = i.getGrandTotal();

		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

		assertTrue(s.contains(uuid.toString()));
		assertTrue(s.contains("2024-12-25"));
		assertTrue(s.contains(company.toString()));
		assertTrue(s.contains(john.toString()));
	}

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in the VGB
	 * system.
	 */
	@Test
	public void testInvoice02() {

		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;

		LocalDate leaseStart = LocalDate.parse("2024-01-01");
		LocalDate leaseEnd = LocalDate.parse("2026-06-01");

		LeasedEquipment le = new LeasedEquipment(uuid, name, model, cost, leaseStart, leaseEnd);

		uuid = UUID.randomUUID();
		name = "Backhoe 3000";
		model = "BH30X2";
		cost = 95125.00;
		double hoursRented = 25;

		RentedEquipment re = new RentedEquipment(uuid, name, model, cost, hoursRented);

		List<Billable> list = new ArrayList<>();
		list.add(le);
		list.add(re);

		uuid = UUID.randomUUID();
		String first = "John";
		String last = "Smith";
		String email1 = "jsmith1@gmail.com";
		String email2 = "john@hotmail.com";
		List<String> listEmails = new ArrayList<>();
		listEmails.add(email1);
		listEmails.add(email2);
		Person john = new Person(uuid, first, last, null, listEmails);

		uuid = UUID.randomUUID();
		String jasonFirst = "Jason";
		String jasonLast = "Smith";
		String jemail1 = "jasonsmith1@gmail.com";
		String jemail2 = "jason@hotmail.com";
		List<String> jasonEmails = new ArrayList<>();
		jasonEmails.add(jemail1);
		jasonEmails.add(jemail2);
		Person jason = new Person(uuid, jasonFirst, jasonLast, null, jasonEmails);

		UUID companyUUID = UUID.randomUUID();
		Person contactPerson = jason;
		String compName = "The Best Company";

		String street = "1034 West Oak Rd";
		String city = "Lincoln";
		String state = "NE";
		String zip = "90029";

		Address a = new Address(street, city, state, zip);

		Company company = new Company(companyUUID, contactPerson, compName, a);

		uuid = UUID.randomUUID();
		LocalDate date = LocalDate.of(2024, 12, 25);

		Invoice i = new Invoice(uuid, date, company, john, list);

		String s = i.toString();

		double expectedSubtotal = 71415.42;
		double expectedTaxTotal = 1604.16;
		double expectedGrandTotal = 73019.58;

		double actualSubtotal = i.getSubTotal();
		double actualTaxTotal = i.getTaxTotal();
		double actualGrandTotal = i.getGrandTotal();

		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

		assertTrue(s.contains(uuid.toString()));
		assertTrue(s.contains("2024-12-25"));
		assertTrue(s.contains(company.toString()));
		assertTrue(s.contains(john.toString()));
	}

}