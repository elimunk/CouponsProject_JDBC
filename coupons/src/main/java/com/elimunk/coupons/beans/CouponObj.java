package com.elimunk.coupons.beans;

public class CouponObj {

	private Coupon coupon;
	private String token;
	
	public CouponObj(Coupon coupon, String token) {
		this.coupon = coupon;
		this.token = token;
	}
	
	public CouponObj() {
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "Coupon: " + getCoupon().toString() + "\nToken: " + getToken();
	}
	
	
}
