package com.eli.coupons.beans;

public class Company {

	// Properties

	private long id;
	private String name;
	private String logo;

	// constructor - (using setters)

	public Company(String name, String logo) {
		this.name = name;
		this.logo = logo;
	}

	// Default constructor
	public Company() {
	}

	// setters & getters

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	// methods

	@Override
	public String toString() {
		return "Company Id: " + getId() + ", Company Name: " + getName() + ", Logo: " + getLogo() + "\n";
	}

}
