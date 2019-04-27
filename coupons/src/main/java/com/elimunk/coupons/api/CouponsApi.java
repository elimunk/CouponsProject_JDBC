package com.elimunk.coupons.api;

import java.util.List;

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
import com.elimunk.coupons.enums.Category;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.CouponsController;

@RestController
@RequestMapping("/coupons")
public class CouponsApi {

	@Autowired
	private CouponsController couponController;
	
	@PostMapping
	public void createCoupon(@RequestBody Coupon coupon) throws ApplicationException {
		couponController.createCoupon(coupon);
		System.out.println(coupon);
	}

	@PutMapping("/userCompanyId")
	public void updateCoupon( @RequestParam("userCompanyId") long userCompanyId ,@RequestBody Coupon couponToUpdate) throws ApplicationException {
		couponController.updateCoupon(couponToUpdate, userCompanyId);
	}
	
	@DeleteMapping("/{couponId}")
	public void deleteCoupon(@PathVariable("couponId") long id ,  @RequestParam("userCompanyId") long userCompanyId) throws ApplicationException {
		couponController.deleteCoupon(id, userCompanyId);;
	}

	@GetMapping("/{couponId}")
	public Coupon getCoupon(@PathVariable("couponId") long id) throws ApplicationException {
		return couponController.getCoupon(id);
	}
	
	@GetMapping("/all")
	public List<Coupon> getAllCoupons() throws ApplicationException {
		return couponController.getAllCoupons();
	}
	
	@GetMapping("/byCompanyId")
	public List<Coupon> getCompanyCoupons( @RequestParam("id") long companyId) throws ApplicationException {
		return couponController.getCompanyCoupons(companyId);
	}
	
	@GetMapping("/byCompanyIdAndMaxPrice")
	public List<Coupon> getCompanyCouponsByMaxPrice( @RequestParam("id") long companyId, @RequestParam("maxPrice") double maxPrice) throws ApplicationException {
		return couponController.getCompanyCouponsByMaxPrice(companyId, maxPrice);
	}
	
	@GetMapping("/byCompanyIdAndCategory")
	public List<Coupon> getCompanyCouponsByCategory( @RequestParam("id") long id , @RequestParam("category") Category couponType) throws ApplicationException{
		return couponController.getCompanyCouponsByCategory( id , couponType);
	}
	
	@GetMapping("/byCustomerId")
	public List<Coupon> getCustomerCoupons(@RequestParam("id") long customerId) throws ApplicationException {
		return couponController.getCustomerCoupons(customerId);
	}
	
	@GetMapping("/byCustomerIdAndMaxPrice")
	public List<Coupon> getCustomerCouponsByMaxPrice(@RequestParam("id") long customerId, @RequestParam("maxPrice") double maxPrice) throws ApplicationException {
		return couponController.getCustomerCouponsByMaxPrice(customerId, maxPrice);
	}
	
	@GetMapping("/byCustomerIdAndCategory")
	public List<Coupon> getCustomerCouponsByCategory(@RequestParam("id") long customerId, @RequestParam("category") Category category) throws ApplicationException {
		return couponController.getCustomerCouponsByCategory(customerId, category);
	}

}
