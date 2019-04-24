package com.eli.coupons.beans;

public class Customer {

	// Properties

	private long Id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private User userCustomer;

	// constructor

	public Customer(String firstName, String lastName, String email, String phoneNumber, User userCustomer) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.userCustomer = userCustomer;
	}
	

	public Customer(long id, String firstName, String lastName, String email, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}



	// Default constructor
	public Customer() {
	}

	// setters & getters

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		this.Id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public User getUserCustomer() {
		return userCustomer;
	}

	public void setUserCustomer(User userCustomer) {
		this.userCustomer = userCustomer;
	}
	
	// methods

	@Override
	public String toString() {
		return "Cusromer Id: " + getId() + ", First Name: " + getFirstName() + ", Last Name: " + getLastName()
				+ ", Email: " + getEmail()+ ", Phone number: " + getPhoneNumber() /* + "Login details: "+ userCustomer.toString() */+ "\n" ;
	}

}
