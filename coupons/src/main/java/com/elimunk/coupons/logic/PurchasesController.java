package com.elimunk.coupons.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.elimunk.coupons.beans.Coupon;
import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.beans.Purchase;
import com.elimunk.coupons.dao.CouponsDao;
import com.elimunk.coupons.dao.CustomersDao;
import com.elimunk.coupons.dao.PurchasesDao;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.utils.DateUtils;

//this is the purchases logic level to control of all operations of the purchases 
@Controller
public class PurchasesController {

// property
	
	// Instances of 'Dao' levels for the purchase operations 
	@Autowired
	private CouponsDao couponsDao;
	@Autowired
	private PurchasesDao purchasesDao;
	@Autowired
	CustomersDao customerDao;

	public PurchasesController() {
		super();
	}

//	methods
	
	public long createPurchace(Purchase purchase, PostLoginUserData userData) throws ApplicationException {
		if (userData.getClientType().name() != "CUSTOMER" && userData.getClientType().name() != "ADMINISTRATOR") {
			throw new ApplicationException(ErrorTypes.HACKING_ERROR, DateUtils.getCurrentDateAndTime(),
					" HACKING! ONLY CUSTOMER CAN PURCHASE! USER WITHOUT PERMISSION TRYING TO PURCHASE" ,true);
		}
		// validate the purchase before the creating 
		validatePurchase(purchase);
		// if the purchase is valid - add to the database.
		long purchaseId = purchasesDao.addPurchase(purchase);
		// after purchase update the coupon amount
		couponsDao.updateCouponAmount(purchase.getCouponId(), purchase.getAmount());
		
		System.out.println("Purchase No " + purchase.getId() + " created successfully");
		return purchaseId;
	}

	public void deletePurchase(long purchaseId, PostLoginUserData userData) throws ApplicationException {
		if (userData.getClientType().name() != "ADMINISTRATOR") {
			throw new ApplicationException(ErrorTypes.HACKING_ERROR, DateUtils.getCurrentDateAndTime(),
					" HACKING! USER WITHOUT PERMISSION TRYING TO DELETE PURCHASE!" ,true);
		}
		// Check if the purchase exists. before the action
		validateExistPurchase(purchaseId);
		// Delete the purchase in the database 
		purchasesDao.deletePurchaseById(purchaseId);
	}

	public Purchase getPurchase(long purchaseId) throws ApplicationException {
		// Check if the purchase exists. before the action
		validateExistPurchase(purchaseId);
		// Get the purchase from the database
		return purchasesDao.getPurchase(purchaseId);
	}
	
	public List<Purchase> getAllPurchases() throws ApplicationException {
		return purchasesDao.getAllPurchases();
	}
	
	public List<Purchase> getCompanyPurchases( long companyId, PostLoginUserData userData) throws ApplicationException {
		if (userData.getClientType().name() != "ADMINISTRATOR") {
			return purchasesDao.getCompanyPurchases(companyId);
		}
		if (userData.getClientType().name() == "COMPANY" && companyId == userData.getCompanyId()) {
			return purchasesDao.getCompanyPurchases(companyId);
		}
		else throw new ApplicationException(ErrorTypes.HACKING_ERROR, DateUtils.getCurrentDateAndTime(),
					" HACKING! company id is not correct!!" ,true);
	}
	
	public List<Purchase> getCustomerPurchases(long customerId, PostLoginUserData userData) throws ApplicationException {
		if (userData.getClientType().name() != "ADMINISTRATOR") {
			return purchasesDao.getCustomerPurchases(customerId);
		}
		if (userData.getClientType().name() == "CUSTOMER" && customerId == userData.getCompanyId()) {
			return purchasesDao.getCustomerPurchases(customerId);
		}
		else throw new ApplicationException(ErrorTypes.HACKING_ERROR, DateUtils.getCurrentDateAndTime(),
					" HACKING! customer id is not correct!!" ,true);
	}
	
	
	// validate the purchase before adding or updating .
	private void validatePurchase(Purchase purchase) throws ApplicationException {
		Coupon coupon = couponsDao.getCoupon(purchase.getCouponId());
		
		// the coupon purchase has to exist 
		if (!couponsDao.isCouponExistById(purchase.getCouponId())) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The coupon not exist! " ,false);
		}
		// the customer has to exist 
		if (!customerDao.isCustomerExsistById(purchase.getCustomerId())) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The customer not exist! " ,false);
		}
		// checks that the coupon not expired
		if (coupon.getEndDate().before(new Date())) {
			throw new ApplicationException(ErrorTypes.OUT_OF_STOCK, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The coupon is expired" ,false);
		}
		// the coupon amount must be higher than 0
		if (coupon.getAmount() <= 0) {
			throw new ApplicationException(ErrorTypes.OUT_OF_STOCK, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The coupon is not available for purchase (amount 0)" ,false);
		}
		// puechase amount must be positive number
		if (purchase.getAmount() <= 0) {
			throw new ApplicationException(ErrorTypes.VALUE_OF_ZERO, DateUtils.getCurrentDateAndTime(), "Create purchase failed. puechase amount must be a positive number" ,false);
		}
		// the purchase amount must be under than the coupon amount
		if (purchase.getAmount() > coupon.getAmount()) {
			throw new ApplicationException(ErrorTypes.INVALID_AMOUNT, DateUtils.getCurrentDateAndTime(), "Create purchase failed. amount of puechase is bigger than the coupon amount)" ,false);
		}
	}
	
	// validate that the purchase exist
	private void validateExistPurchase(long purchaseId) throws ApplicationException {
		if (!purchasesDao.isPurchaseExistById(purchaseId)) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "This purchase does not exist" ,false);
		}
	}
	
}
