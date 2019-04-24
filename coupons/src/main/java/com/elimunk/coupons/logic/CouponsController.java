package com.elimunk.coupons.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.elimunk.coupons.beans.Coupon;
import com.elimunk.coupons.dao.CompaniesDao;
import com.elimunk.coupons.dao.CouponsDao;
import com.elimunk.coupons.dao.PurchasesDao;
import com.elimunk.coupons.enums.Category;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.utils.DateUtils;

//this is the coupons logic level to control of all operations of the coupons 
@Controller
public class CouponsController {

// properties
	
	// Instances of 'dao' levels for the operations 
	@Autowired
	private CouponsDao couponDao = new CouponsDao();
	@Autowired
	private CompaniesDao companyDao = new CompaniesDao();
	@Autowired
	private PurchasesDao purchasesDao = new PurchasesDao();
	
	
	public CouponsController() {
		super();
	}

//	methods

	public void createCoupon(Coupon coupon) throws ApplicationException {
		// first we confirm that the coupon is valid to create
		validateCoupon(coupon);
		// this is to validate title
		validateTitleCoupon(coupon);
		// if coupon is valid - add to the database.
		couponDao.addCoupon(coupon);
		System.out.println("Coupon No " + coupon.getId() + " created successfully");
	}
	
	public void updateCoupon(Coupon couponToUpdate, long userCompanyId) throws ApplicationException {
		// Check if the coupon exists. before the action
		validateExistCoupon(couponToUpdate.getId());
		// checks that the user have the correct company Id
		validateUserCompanyId(couponToUpdate.getCompanyId(), userCompanyId);
		// Check if the coupon changes is valid for update. before the action
		validateCoupon(couponToUpdate);
		// Update the coupon in the database
		couponDao.updateCoupon(couponToUpdate);
	}

	public void deleteCoupon(long couponId, long userCompanyId) throws ApplicationException {
		// Check if the coupon exists. before the action
		validateExistCoupon(couponId);
		// checks that the user have the correct company Id
		validateUserCompanyId(couponDao.getCoupon(couponId).getCompanyId(), userCompanyId);
		// Delete the purchase of coupon in the database
		purchasesDao.deletePurchaseByCouponId(couponId);
		// Delete the coupon in the database
		couponDao.deleteCoupon(couponId);
	}
	
	// this is deleting all of the expired coupons (used by daily job)
	public void deleteExpiredCoupon() throws ApplicationException {
		couponDao.deleteExpiredCoupons();
	}
	
	public Coupon getCoupon(long couponId) throws ApplicationException {
		// Check if the coupon exists. before the action
		validateExistCoupon(couponId);
		// Get the coupon from the database
		return couponDao.getCoupon(couponId);
	}

	public List<Coupon> getAllCoupons() throws ApplicationException {
		List<Coupon> coupons = couponDao.getAllCoupons();
		return coupons;
	}
	
	public List<Coupon> getCompanyCoupons(long companyId) throws ApplicationException {
		List<Coupon> coupons = couponDao.getAllCoupons();
		return coupons;
	}

	public List<Coupon> getCompanyCouponsByCategory(long companyId, Category category) throws ApplicationException {
		List<Coupon> coupons = couponDao.getCompanyCouponsByCategory(companyId, category);
		return coupons;
	}
	
	public List<Coupon> getCompanyCouponsByMaxPrice(long companyId, double maxPrice) throws ApplicationException {
		List<Coupon> coupons = couponDao.getCompanyCouponsByMaxPrice(companyId, maxPrice);
		return coupons;
	}
	
	public List<Coupon> getCustomerCoupons(long customerId) throws ApplicationException {
		return couponDao.getCustomerCoupons(customerId);
	}
	
	public List<Coupon> getCustomerCouponsByMaxPrice(long customerId, double maxPrice) throws ApplicationException {
		return couponDao.getCustomerCouponsByMaxPrice(customerId, maxPrice);
	}
	
	public List<Coupon> getCustomerCouponsByCategory(long customerId, Category category) throws ApplicationException {
		return couponDao.getCustomerCouponsByCategory(customerId, category);
	}

	
	/* validate the coupons before adding or updating .
	 * coupon companyId - the company has to exist
	 * coupon description - must contain minimum 15 characters
	 * coupon endDate - cannot be earlier than the Start date or current date
	 * coupon amount - must be a positive number
	 * coupon price - must be a positive number
	 * if any parameter is invalid - ApplicationException will be thrown
	 * */ 
	private void validateCoupon(Coupon coupon) throws ApplicationException {
		if (!companyDao.isCompanyExistById(coupon.getCompanyId())) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(),
					"The companyId invalid! This company does not exsit");
		}
		if (coupon.getDescription().length() < 15) {
			throw new ApplicationException(ErrorTypes.INVALID_DESCRPTION, DateUtils.getCurrentDateAndTime(),
					"Description '" + coupon.getDescription() + "' is not valid !must contain minimum 15 characters!");
		}
		if (coupon.getEndDate().before(coupon.getStartDate()) || coupon.getEndDate().before(new Date())) {
			throw new ApplicationException(ErrorTypes.INVALID_END_DATE, DateUtils.getCurrentDateAndTime(),
					"Date '" + coupon.getEndDate()
							+ "' is not valid! the end date cannot be earlier than the Start date or current date");
		}
		if (coupon.getAmount() <= 0) {
			throw new ApplicationException(ErrorTypes.VALUE_OF_ZERO, DateUtils.getCurrentDateAndTime(),
					"Cant create coupon with amount 0 or under ");
		}
		if (coupon.getPrice() <= 0) {
			throw new ApplicationException(ErrorTypes.VALUE_OF_ZERO, DateUtils.getCurrentDateAndTime(),
					"The price value must contain a positive number");
		}
	}
	
	// validate coupon title that not already exist, and that contain 2 - 50 characters
	private void validateTitleCoupon(Coupon coupon) throws ApplicationException {
		if (couponDao.isCouponExistByTitle(coupon.getTitle())) {
			throw new ApplicationException(ErrorTypes.TITLE_EXIST, DateUtils.getCurrentDateAndTime(),
					"The title of the coupon already exsit");
		}
		if (coupon.getTitle().length() > 50 || coupon.getTitle().length() < 2) {
			throw new ApplicationException(ErrorTypes.INVALID_TITLE, DateUtils.getCurrentDateAndTime(),
					"Title '" + coupon.getTitle() + "' is not valid !must contain 2 - 50 characters!");
		}
	}
	
	// validate that the coupon exist
	private void validateExistCoupon(long couponId) throws ApplicationException {
		if (!couponDao.isCouponExistById(couponId)) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "The coupon is not exsit");
		}
	}

	// checks that the user have the correct company Id before  operation on the coupon
	private void validateUserCompanyId(long couponCompanyId , long userCompanyId) throws ApplicationException {
		if (couponCompanyId != userCompanyId) {
			// if the user company Id is not correct throw HACKING ERROR
			throw new ApplicationException(ErrorTypes.HACKING_ERROR, DateUtils.getCurrentDateAndTime(),
					" HACKING! company id is not corrcet!");
		}
	}

}
