package com.vgb;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents an equipment item that is leased
 */
public class LeasedEquipment extends Equipment {

	private LocalDate leaseStart;
	private LocalDate leaseEnd;

	public LeasedEquipment(UUID uuid, String name, String modelNumber, double retailPrice, LocalDate leaseStart,
			LocalDate leaseEnd) {
		super(uuid, name, modelNumber, retailPrice);
		this.leaseStart = leaseStart;
		this.leaseEnd = leaseEnd;
	}

	public LeasedEquipment(Equipment e, LocalDate startDate, LocalDate endDate) {
		super(e.getUuid(), e.getName(), e.getModelNumber(), e.getRetailPrice());
		this.leaseStart = startDate;
		this.leaseEnd = endDate;
	}

	public LocalDate getLeaseStart() {
		return leaseStart;
	}

	public LocalDate getLeaseEnd() {
		return leaseEnd;
	}

	@Override
	public String toString() {
		return String.format("""
				%s (Lease)       %s-%s
					%.0f days (%s -> %s)
				                                                         $   %.2f $  %.2f
				""", this.getUuid(), this.getName(), this.getModelNumber(), getDays(), leaseStart.toString(),
				leaseEnd.toString(), getTaxes(), getCost());
	}

	/**
	 * Returns the number of days between the lease start and lease end, inclusive
	 * on both days
	 * 
	 * @return
	 */
	public double getDays() {
		return ChronoUnit.DAYS.between(leaseStart, leaseEnd) + 1;
	}

	/**
	 * Returns the cost of a leased equipment item
	 */
	@Override
	public double getCost() {
		double daysBetween = this.getDays();
		double lengthInYears = daysBetween / 365;

		double cost = ((lengthInYears / 5) * getRetailPrice() * 1.5);
		double roundedCost = Math.round(cost * 100.0) / 100.0;

		return roundedCost;
	}

	/**
	 * Returns the tax of a leased equipment item
	 */
	@Override
	public double getTaxes() {
		if (getCost() > 12500) {
			return 1500;
		} else {
			return 0;
		}
	}

}
