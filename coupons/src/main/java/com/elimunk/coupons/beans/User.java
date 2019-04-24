package com.elimunk.coupons.beans;

import com.elimunk.coupons.enums.ClientType;

public class User {
	
	// Properties

	private long id;
	private String userName;
	private String password;
	private Long companyId;
	private ClientType type;
	
	// constructors

	public User(String userName, String password, Long companyId, ClientType type) {
		this(userName, password, type);
		this.companyId = companyId;
	}
	
	public User(String userName, String password, ClientType type) {
		this.userName = userName;
		this.password = password;
		this.type = type;
	}
	
	public User() {
	}

	// setters & getters

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public ClientType getType() {
		return type;
	}

	public void setType(ClientType type) {
		this.type = type;
	}
	
	private String printCompanyIdIfExsit() {
		return (this.getCompanyId() !=null && this.getCompanyId() !=0) ?", CompanyId: " + getCompanyId() : "";
	}
	
	@Override
	public String toString() {
		return "User Id: " + getId() + ", Username: " + getUserName() + ", Type: " + getType()
				+ printCompanyIdIfExsit() + "\n";
	}
}

