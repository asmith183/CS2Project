package com.vgb;

import java.util.UUID;

/**
 * Represents a material item
 */
public class Material extends Item {

	private String unit;
	private double costPerUnit;
	private double numUnits;

	public Material(UUID uuid, String name, String unit, double costPerUnit) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
	}

	public Material(UUID uuid, String name, String unit, double costPerUnit, double numUnits) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
		this.numUnits = numUnits;
	}

	public Material(Material m, double numUnits) {
		super(m.getUuid(), m.getName()); 
	    this.unit = m.unit;
	    this.costPerUnit = m.costPerUnit;
	    this.numUnits = numUnits;
	}

	public String getUnit() {
		return unit;
	}

	public double getCostPerUnit() {
		return costPerUnit;
	}

	public double getNumUnits() {
		return numUnits;
	}

	@Override
	public String toString() {
		return String.format("""
				%s (Material) %s
					%.0f @ $%.2f/%s
				                                                         $   %.2f $  %.2f
				""", getUuid(), getName(), numUnits, costPerUnit, getUnit(), getTaxes(), getCost());
	}

	/**
	 * Returns the cost of a material item
	 */
	@Override
	public double getCost() {
		double cost = costPerUnit * numUnits;

		return cost;
	}

	/**
	 * Returns the tax of a material item
	 */
	@Override
	public double getTaxes() {
		double taxes = (costPerUnit * numUnits) * 0.0715;
		double roundedTaxes = Math.round(taxes * 100.0) / 100.0;

		return roundedTaxes;
	}

}
