package com.vgb;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Aden Smith 2025-05-06
 * 
 * Produces a report on the invoice data from the database. Utilizes the custom
 * sorted linked list implementation.
 * 
 */
public class InvoiceReport {

	public static void main(String[] args) {

		Map<UUID, Company> companyMap = DatabaseLoader.loadCompanyData();

		Comparator<Invoice> byTotalDescending = Comparator.comparingDouble(Invoice::getGrandTotal).reversed()
				.thenComparing(Invoice::getUuid);

		Comparator<Invoice> byCustomerName = Comparator.comparing((Invoice i) -> i.getCustomer().getName())
				.thenComparing(Invoice::getUuid);

		SortedLinkedList<Invoice> invoiceListTotal = DatabaseLoader.loadInvoices(byTotalDescending);
		SortedLinkedList<Invoice> invoiceListCustomerName = DatabaseLoader.loadInvoices(byCustomerName);

		Comparator<Company> byCompanyInvoiceTotal = Comparator.comparingDouble(Company::getTotalAcrossInvoices)
				.thenComparing(Company::getCompanyUUID);

		SortedLinkedList<Company> companyListByTotal = new SortedLinkedList<>(byCompanyInvoiceTotal);

		for (Company company : companyMap.values()) {
			company.updateTotalAcrossInvoices(invoiceListTotal);
			company.updateNumInvoices(invoiceListTotal);
			companyListByTotal.add(company);
		}

		System.out.println("+-------------------------------------------------------------------------+\n"
				+ "| Invoices by Total                                                       |\n"
				+ "+-------------------------------------------------------------------------+\n"
				+ "Invoice                              Customer                  Total     ");
		for (Invoice i : invoiceListTotal) {
			System.out.printf("%-36s %-25s $ %9.2f\n", i.getUuid(), i.getCustomer().getName(), i.getGrandTotal());
		}

		System.out.println("+-------------------------------------------------------------------------+\n"
				+ "| Invoices by Customer                                                    |\n"
				+ "+-------------------------------------------------------------------------+\n"
				+ "Invoice                              Customer                  Total     ");

		for (Invoice i : invoiceListCustomerName) {
			System.out.printf("%-36s %-25s $ %9.2f\n", i.getUuid(), i.getCustomer().getName(), i.getGrandTotal());
		}

		System.out.println("+-------------------------------------------------------------------------+\n"
				+ "| Customer Invoice Totals                                                 |\n"
				+ "+-------------------------------------------------------------------------+\n"
				+ "Customer                  Number of Invoices        Total");

		for (Company c : companyListByTotal) {
			System.out.printf("%-25s %15d          $ %12.2f\n", c.getName(), c.getNumInvoices(),
					c.getTotalAcrossInvoices());
		}

	}
}
