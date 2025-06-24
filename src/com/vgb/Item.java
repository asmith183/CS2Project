package com.vgb;

import java.util.UUID;

/**
 * Represents an item
 */
public abstract class Item implements Billable {

	private UUID uuid;
	private String name;

	public Item(UUID uuid, String name) {
		super();
		this.uuid = uuid;
		this.name = name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Item [uuid=" + uuid + ", name=" + name + "]";
	}

	/**
	 * Returns the cost of an item
	 */
	public abstract double getCost();

	/**
	 * Returns the tax of an item
	 */
	public abstract double getTaxes();

}
