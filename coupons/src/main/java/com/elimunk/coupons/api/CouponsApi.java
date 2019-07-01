package com.elimunk.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elimunk.coupons.beans.Coupon;
import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.enums.Category;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.CouponsController;

@RestController
@RequestMapping("/coupons")
public class CouponsApi {

	@Autowired
	private CouponsController couponController;
	
	@PostMapping
	public long createCoupon(@RequestBody Coupon coupon , HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		coupon.setCompanyId(userData.getCompanyId());
		return couponController.createCoupon(coupon, userData);
	}

	@PutMapping
	public void updateCoupon(@RequestBody Coupon couponToUpdate , HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		couponController.updateCoupon(couponToUpdate, userData);
	}
	
	@DeleteMapping("/{couponId}")
	public void deleteCoupon(@PathVariable("couponId") long id , HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		couponController.deleteCoupon(id, userData);
	}

	@GetMapping("/{couponId}")
	public Coupon getCoupon(@PathVariable("couponId") long id , HttpServletRequest request) throws ApplicationException {
		return couponController.getCoupon(id);
	}
	
	@GetMapping
	public List<Coupon> getAllCoupons() throws ApplicationException {
		return couponController.getAllCoupons();
	}
	
	@GetMapping("/byCompany")
	public List<Coupon> getCompanyCoupons(@RequestParam("id") long companyId) throws ApplicationException {
		return couponController.getCompanyCoupons(companyId);
	}
	
	@GetMapping("/byCompany/maxPrice")
	public List<Coupon> getCompanyCouponsByMaxPrice(@RequestParam("id") long companyId , @RequestParam("maxPrice") double maxPrice) throws ApplicationException {
		return couponController.getCompanyCouponsByMaxPrice(companyId, maxPrice);
	}
	
	@GetMapping("/byCompany/category")
	public List<Coupon> getCompanyCouponsByCategory(@RequestParam("id") long companyId , @RequestParam("category") Category category) throws ApplicationException{
		return couponController.getCompanyCouponsByCategory(companyId, category);
	}
	
	@GetMapping("/byCustomer")
	public List<Coupon> getCustomerCoupons(@RequestParam("id") long customerId) throws ApplicationException {
		return couponController.getCustomerCoupons(customerId);
	}
	
	@GetMapping("/byCustomer/MaxPrice")
	public List<Coupon> getCustomerCouponsByMaxPrice(@RequestParam("id") long customerId, @RequestParam("maxPrice") double maxPrice) throws ApplicationException {
		return couponController.getCustomerCouponsByMaxPrice(customerId, maxPrice);
	}
	
	@GetMapping("/byCustomer/Category")
	public List<Coupon> getCustomerCouponsByCategory(@RequestParam("id") long customerId, @RequestParam("category") Category category) throws ApplicationException {
		return couponController.getCustomerCouponsByCategory(customerId, category);
	}

}
