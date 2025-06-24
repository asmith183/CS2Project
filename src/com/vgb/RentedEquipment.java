package com.vgb;

import java.util.UUID;

/**
 * Represents an equipment item that is rented
 */
public class RentedEquipment extends Equipment {

	private double hoursRented;

	public RentedEquipment(UUID uuid, String name, String modelNumber, double retailPrice, double hoursRented) {
		super(uuid, name, modelNumber, retailPrice);
		this.hoursRented = hoursRented;
	}

	public RentedEquipment(Equipment e, double hoursRented) {
		super(e.getUuid(), e.getName(), e.getModelNumber(), e.getRetailPrice());
		this.hoursRented = hoursRented;
	}

	public double getHoursRented() {
		return hoursRented;
	}

	public double getCostPerHour() {
		return 0.001 * getRetailPrice();
	}

	@Override
	public String toString() {
		return String.format("""
				%s (Rental)       %s-%s
					%.0f hours @ $%.2f per hour
				                                                         $   %.2f $ %.2f
				""", this.getUuid(), this.getName(), this.getModelNumber(), hoursRented, getCostPerHour(), getTaxes(),
				getCost());
	}

	/**
	 * Returns the cost of an equipment item that is rented
	 */
	@Override
	public double getCost() {
		double costPerHour = this.getCostPerHour();
		double cost = costPerHour * hoursRented;
		double roundedCost = Math.round(cost * 100.0) / 100.0;

		return roundedCost;
	}

	/**
	 * Returns the tax of an equipment item that is rented
	 */
	@Override
	public double getTaxes() {
		double taxes = getCost() * 0.0438;
		double roundedTaxes = Math.round(taxes * 100.0) / 100.0;

		return roundedTaxes;
	}

}
