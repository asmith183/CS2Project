package com.vgb;

import java.util.UUID;

/**
 * Represents an equipment item
 */
public class Equipment extends Item {

	private String modelNumber;
	private double retailPrice;

	public Equipment(UUID uuid, String name, String modelNumber, double retailPrice) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailPrice = retailPrice;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	@Override
	public String toString() {
		return String.format("""
				%s (Purchase)       %s-%s
				                                                         $   %.2f $ %.2f
				""", this.getUuid(), this.getName(), modelNumber, this.getTaxes(), this.getCost());
	}

	/**
	 * Returns the cost of an equipment item
	 */
	@Override
	public double getCost() {
		return retailPrice;
	}

	/**
	 * Returns the tax of an equipment item
	 */
	@Override
	public double getTaxes() {
		double taxes = Math.round((retailPrice * 0.0525) * 100.0) / 100.0;
		return taxes;
	}

}
