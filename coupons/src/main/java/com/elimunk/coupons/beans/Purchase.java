package com.elimunk.coupons.beans;

public class Purchase {

	// Properties

	private long id;
	private long customerId;
	private long couponId;
	private int amount;

	// constructors

	public Purchase(long customerId, long couponId, int amount) {
		this.customerId = customerId;
		this.couponId = couponId;
		this.amount = amount;
	}

	public Purchase() {
	}

	// setters & getters

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "Purchase Id: " + getId() + " Customer Id: " + getCustomerId() 
		+ " Coupon Id: " + getCouponId() + " Amount: " + getAmount() +"\n";
	}
	
}
