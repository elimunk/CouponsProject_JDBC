package com.elimunk.coupons.logic;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.elimunk.coupons.beans.Coupon;
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
	private CouponsDao couponsDao = new CouponsDao();
	private PurchasesDao purchasesDao = new PurchasesDao();

	
	public PurchasesController() {
		super();
	}

//	methods
	
	public void createPurchace(Purchase purchase) throws ApplicationException {
		// validate the purchase before the creating 
		validatePurchase(purchase);
		// if the purchase is valid - add to the database.
		purchasesDao.addPurchase(purchase);
		// after purchase update the coupon amount
		couponsDao.uptateCouponAmount(purchase.getCouponId(), purchase.getAmount());
		System.out.println("Purchase No " + purchase.getId() + " created successfully");
	}

	public Purchase getPurchase(long purchaseId) throws ApplicationException {
		// Check if the purchase exists. before the action
		validateExistPurchase(purchaseId);
		// Get the purchase from the database
		return purchasesDao.getPurchase(purchaseId);
	}
	
	public void deletePurchase(long purchaseId) throws ApplicationException {
		// Check if the purchase exists. before the action
		validateExistPurchase(purchaseId);
		// Delete the purchase in the database 
		purchasesDao.deletePurchaseById(purchaseId);
	}
	
	public List<Purchase> getAllPurchases() throws ApplicationException {
		return purchasesDao.getAllPurchases();
	}
	
	// validate the purchase before adding or updating .
	private void validatePurchase(Purchase purchase) throws ApplicationException {
		CustomersDao customerDao = new CustomersDao();
		Coupon coupon = couponsDao.getCoupon(purchase.getCouponId());
		
		// the coupon purchase has to exist 
		if (!couponsDao.isCouponExistById(purchase.getCouponId())) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The coupon not exist! ");
		}
		// the customer has to exist 
		if (!customerDao.isCustomerExsistById(purchase.getCustomerId())) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The customer not exist! ");
		}
		// checks that the coupon not expired
		if (coupon.getEndDate().before(new Date())) {
			throw new ApplicationException(ErrorTypes.OUT_OF_STOCK, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The coupon is expired");
		}
		// the coupon amount must be higher than 0
		if (coupon.getAmount() <= 0) {
			throw new ApplicationException(ErrorTypes.OUT_OF_STOCK, DateUtils.getCurrentDateAndTime(), "Create purchase failed. The coupon is not available for purchase (amount 0)");
		}
		// puechase amount must be positive number
		if (purchase.getAmount() <= 0) {
			throw new ApplicationException(ErrorTypes.VALUE_OF_ZERO, DateUtils.getCurrentDateAndTime(), "Create purchase failed. puechase amount must be a positive number");
		}
		// the purchase amount must be under than the coupon amount
		if (purchase.getAmount() > coupon.getAmount()) {
			throw new ApplicationException(ErrorTypes.INVALID_AMOUNT, DateUtils.getCurrentDateAndTime(), "Create purchase failed. amount of puechase is bigger than the coupon amount)");
		}
	}
	
	// validate that the purchase exist
	private void validateExistPurchase(long purchaseId) throws ApplicationException {
		if (!purchasesDao.isPurchaseExistById(purchaseId)) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "This purchase does not exist");
		}
	}
	
}
