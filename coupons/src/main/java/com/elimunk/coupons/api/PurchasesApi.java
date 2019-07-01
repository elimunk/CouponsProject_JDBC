package com.elimunk.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.beans.Purchase;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.PurchasesController;

@RestController
@RequestMapping("/purchases")
public class PurchasesApi {

	@Autowired
	private PurchasesController purchasesController;
	
	@PostMapping
	public long createPurchace(@RequestBody Purchase purchase, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		return purchasesController.createPurchace(purchase, userData);
	}
	
	@DeleteMapping("/{purchaseId}")
	public void deletePurchase(@PathVariable ("purchaseId") long id, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		purchasesController.deletePurchase(id, userData);
	}

	@GetMapping("/{purchaseId}")
	public Purchase getPurchase(@PathVariable long id) throws ApplicationException {
		return purchasesController.getPurchase(id);
	}
	
	@GetMapping
	public List<Purchase> getAllPurchases() throws ApplicationException {
		return purchasesController.getAllPurchases();
	}
	
	@GetMapping("/byCompany")
	public List<Purchase> getCompanyPurchases(@RequestParam("id") long companyId, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		return purchasesController.getCompanyPurchases(companyId, userData);
	}
	
	@GetMapping("/byCustomer")
	public List<Purchase> getCustomerPurchases(@RequestParam("id") long customerId, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		return purchasesController.getCustomerPurchases(customerId, userData);
	}
	
}
