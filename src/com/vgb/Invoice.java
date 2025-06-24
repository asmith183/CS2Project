package com.vgb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an invoice
 */
public class Invoice {

	private UUID uuid;
	private LocalDate date;
	private Company customer;
	private Person salesPerson;
	private List<Billable> billableItems;

	public UUID getUuid() {
		return uuid;
	}

	public LocalDate getDate() {
		return date;
	}

	public Company getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public List<Billable> getBillabeItems() {
		return billableItems;
	}

	public Invoice(UUID uuid, LocalDate date, Company customer, Person salesPerson) {
		super();
		this.uuid = uuid;
		this.date = date;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.billableItems = new ArrayList<>();
	}

	public Invoice(UUID uuid, LocalDate date, Company customer, Person salesPerson, List<Billable> billableItems) {
		super();
		this.uuid = uuid;
		this.date = date;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.billableItems = billableItems;
	}

	@Override
	public String toString() {
		String result = String.format("""
				Invoice#  %s
				Date      %s
				Customer:
				%s
				Sales Person:
				%s
				""", uuid, date, customer.toString(), salesPerson.toString());
		// fix this?
		return result.replace("\n\nSales Person:", "\nSales Person:");
	}
	
	/**
	 * Prints out all of the items on the invoice
	 */
	public void printItems() {
		if (this.getBillabeItems() != null) {
			for (Billable b : this.getBillabeItems()) {
				System.out.println(b);
			}
		}
	}

	/**
	 * Returns the subtotal of the list of billable items in an invoice item
	 * 
	 * @return
	 */
	public double getSubTotal() {
		double total = 0;
		if (this.billableItems == null) {
			return total;
		}
		for (Billable x : billableItems) {
			total += x.getCost();
		}
		return total;
	}

	/**
	 * Returns the tax total of the list of billable items in an invoice item
	 * 
	 * @return
	 */
	public double getTaxTotal() {
		double total = 0;
		if (this.billableItems == null) {
			return total;
		}
		for (Billable x : billableItems) {
			total += x.getTaxes();
		}
		return total;
	}

	/**
	 * Returns the grand total of the list of billable items in an invoice item
	 * 
	 * @return
	 */
	public double getGrandTotal() {
		return this.getSubTotal() + this.getTaxTotal();
	}

}
