package com.vgb;

import java.util.UUID;

/**
 * Represents a contract item
 */
public class Contract extends Item {

	private Company servicer;
	private double amount;

	public Contract(UUID uuid, String name, Company servicer) {
		super(uuid, name);
		this.servicer = servicer;
	}

	public Contract(UUID uuid, String name, Company servicer, double amount) {
		super(uuid, name);
		this.servicer = servicer;
		this.amount = amount;
	}

	public Contract(Contract c, double amount) {
		super(c.getUuid(), c.getName());
		this.servicer = c.servicer;
		this.amount = amount;
	}

	public Company getServicer() {
		return servicer;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return String.format("""
				%s (%s)
				                                                         $      %.2f $  %.2f
				""", getName(), getUuid(), getTaxes(), getCost());
	}

	/**
	 * Returns the cost of a contract
	 */
	@Override
	public double getCost() {
		return amount;
	}

	/**
	 * Returns the taxes of a contract (always 0)
	 */
	@Override
	public double getTaxes() {
		return 0;
	}

}
