package com.elimunk.coupons.beans;

import com.elimunk.coupons.enums.ClientType;

public class PostLoginUserData {

	private Long userId;
	private Long companyId;
	private ClientType clientType;
	
	
	public PostLoginUserData(Long userId, Long companyId, ClientType clientType) {
		this.userId = userId;
		this.companyId = companyId;
		this.clientType = clientType;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Long getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}


	public ClientType getClientType() {
		return clientType;
	}


	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
	
	private String printCompanyIdIfExsit() {
		return (this.getCompanyId() !=null && this.getCompanyId() !=0) ?", CompanyId: " + getCompanyId() : "";
	}
	
	@Override
	public String toString() {
		return "User Id :" + getUserId() +  ", User Type: " + getClientType() + printCompanyIdIfExsit() ;
	}
	
}
