package com.vgb;

import java.util.List;
import java.util.UUID;

/**
 * Represents a company
 */
public class Company {
	private UUID companyUUID;
	private Person contact;
	private String name;
	private Address address;
	private double totalAcrossInvoices;
	private int numInvoices;

	public UUID getCompanyUUID() {
		return companyUUID;
	}

	public Person getContact() {
		return contact;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public double getTotalAcrossInvoices() {
		return totalAcrossInvoices;
	}
	
	public int getNumInvoices()  {
		return this.numInvoices;
	}

	public Company(UUID companyUUID, Person contact, String name, Address address) {
		super();
		this.companyUUID = companyUUID;
		this.contact = contact;
		this.name = name;
		this.address = address;
	}

	@Override
	public String toString() {
		return String.format("""
				%s (%s)
				%s

				%s
				""", name, companyUUID, contact.toString(), address.toString());
	}

	/**
	 * Updates the total of the grand totals of all the invoices with the company as
	 * the customer of all invoices in the given invoiceList.
	 * 
	 * @param invoiceList
	 */
	public void updateTotalAcrossInvoices(SortedLinkedList<Invoice> invoiceList) {
		this.totalAcrossInvoices = 0;
		for (Invoice i : invoiceList) {
			if (i.getCustomer().getCompanyUUID().equals(this.companyUUID)) {
				this.totalAcrossInvoices += i.getGrandTotal();
			}
		}
	}

	/**
	 * Updates the total number of invoices for a company given a sorted linked list
	 * of invoices
	 * 
	 * @param invoiceList
	 */
	public void updateNumInvoices(SortedLinkedList<Invoice> invoiceList) {
		this.numInvoices = 0;
		for (Invoice i : invoiceList) {
			if (i.getCustomer().getCompanyUUID().equals(this.companyUUID)) {
				this.numInvoices++;
			}
		}
	}

}
