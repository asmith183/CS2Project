package com.vgb;

/**
 * Represents an address
 */
public class Address {

	private String street;
	private String city;
	private String state;
	private String zip;

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public Address(String street, String city, String state, String zip) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	@Override
	public String toString() {
		return String.format("""
					%s
					%s %s %s
				""", street, city, state, zip);
	}

}
