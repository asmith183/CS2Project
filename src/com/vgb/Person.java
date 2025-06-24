package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a person
 */
public class Person {

	private UUID uuid;
	private String firstName;
	private String lastName;
	private String phone;
	private List<String> emails;

	public UUID getUuid() {
		return uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public List<String> getEmails() {
		return emails;
	}

	public Person(UUID uuid, String firstName, String lastName, String phone, List<String> emails) {
		super();
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.emails = emails;
	}
	
	public Person(UUID uuid, String firstName, String lastName, String phone) {
		super();
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.emails = new ArrayList<>();
	}

	@Override
	public String toString() {
		return lastName + ", " + firstName + " (" + uuid + ") " + "\n	" + emails;
	}
	
	public void addEmail(String email) {
		this.emails.add(email);
	}

}
