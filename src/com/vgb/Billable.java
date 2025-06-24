package com.vgb;

/**
 * Represents a billable item
 */
public interface Billable {

	/**
	 * Returns the cost of the billable item
	 * 
	 * @return
	 */
	double getCost();

	/**
	 * Returns the taxes of the billable item
	 * 
	 * @return
	 */
	double getTaxes();

}
