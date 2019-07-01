package com.elimunk.coupons.beans;

import com.elimunk.coupons.enums.ClientType;

public class UserIdTypeAndToken {

	private long userId;
	private ClientType userType ;
	private String userToken;
	private Long companyId;
	

	public UserIdTypeAndToken(long userId, ClientType userType, String userToken , Long companyId) {
		this.userId = userId;
		this.userType = userType;
		this.userToken = userToken;
		this.companyId = (companyId!= 0) ? companyId : null;
	}

	public UserIdTypeAndToken() {
	}

	public ClientType getUserType() {
		return userType;
	}
	
	public void setUserType(ClientType userType) {
		this.userType = userType;
	}
	
	public String getUserToken() {
		return userToken;
	}
	
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	
	
	
	
	
	
}
