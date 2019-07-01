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
import org.springframework.web.bind.annotation.RestController;

import com.elimunk.coupons.beans.Customer;
import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.CustomersController;

@RestController
@RequestMapping("/customers")
public class CustomersApi {

	@Autowired
	private CustomersController customersController;
	
	@PostMapping()
	public void createCustomer(@RequestBody Customer customer) throws ApplicationException {
		customersController.createCustomer(customer);
	}
	
	@PutMapping
	public void updateCustomer(@RequestBody Customer customer, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		customersController.updateCustomer(customer, userData);
	}

	@DeleteMapping("/{customerId}")
	public void deleteCustomer(@PathVariable ("customerId") long id, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		customersController.deleteCustomer(id, userData);
	}

	@GetMapping("/{customerId}")
	public Customer getCustomer(@PathVariable ("customerId") long id) throws ApplicationException {
		return customersController.getCustomer(id);
	}
	
	@GetMapping
	public List<Customer> getAllCustomers() throws ApplicationException {
		return customersController.getAllCustomers();
	}
		
}
