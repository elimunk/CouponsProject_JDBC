package com.elimunk.coupons.interfaces;

import java.util.List;

import com.elimunk.coupons.beans.Coupon;
import com.elimunk.coupons.enums.Category;
import com.elimunk.coupons.exceptions.ApplicationException;


public interface ICouponsDao {

	
	boolean isCouponExistByTitle(String title) throws ApplicationException;

	boolean isCouponExistById(long id) throws ApplicationException;

	long addCoupon(Coupon coupon) throws ApplicationException;

	void updateCoupon(Coupon coupon) throws ApplicationException;
	
	void updateCouponAmount(long couponId , int amount) throws ApplicationException;
	
	void deleteCoupon(long couponId) throws ApplicationException;
	
	void deleteCompanyCoupons(long companyId) throws ApplicationException;

	Coupon getCoupon(long couponId) throws ApplicationException;
	
	List<Coupon> getAllCoupons() throws ApplicationException;

	List<Coupon> getCompanyCoupons(long companyId) throws ApplicationException;
	
	List<Coupon> getCompanyCouponsByCategory(long companyId, Category category) throws ApplicationException;

	List<Coupon> getCompanyCouponsByMaxPrice(long companyId, double maxPrice) throws ApplicationException;

	List<Coupon> getCustomerCoupons(long customerId) throws ApplicationException;
	
	List<Coupon> getCustomerCouponsByCategory(long customerId, Category category) throws ApplicationException;

	List<Coupon> getCustomerCouponsByMaxPrice(long customerId, double maxPrice) throws ApplicationException;

}
