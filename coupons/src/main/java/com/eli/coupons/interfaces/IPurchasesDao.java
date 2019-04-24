package com.eli.coupons.interfaces;

import java.util.List;

import com.eli.coupons.beans.Purchase;
import com.eli.coupons.exceptions.ApplicationException;

public interface IPurchasesDao {

	long addPurchase(Purchase purchase) throws ApplicationException;

	void deletePurchaseById(long purchaseId) throws ApplicationException;
	
	void deletePurchaseByCouponId(long couponId) throws ApplicationException;

	void deleteCompanyPurchases(long companyId) throws ApplicationException;

	void deleteCustomerPurchases(long customerId) throws ApplicationException;

	List<Purchase> getAllPurchases() throws ApplicationException;

	Purchase getPurchase(long purchaseId) throws ApplicationException;

	boolean isPurchaseExistById(long purchaseId) throws ApplicationException;
		
}
