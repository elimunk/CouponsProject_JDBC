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
import org.springframework.web.bind.annotation.RestController;

import com.elimunk.coupons.beans.Customer;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.CustomersController;

@RestController
@RequestMapping("/customers")
public class CustomersApi {

	
	@Autowired
	private CustomersController customersController;
	
	@PostMapping
	public void createCustomer(@RequestBody Customer customer) throws ApplicationException {
		customersController.createCustomer(customer);
		System.out.println("Customer No " + customer.getId() + " created successfully");
	}
	
	@PutMapping
	public void updateCustomer(@RequestBody Customer customer) throws ApplicationException {
		customersController.updateCustomer(customer);
	}

	@DeleteMapping("/{customerId}")
	public void deleteCustomer(@PathVariable ("customerId") long customerId) throws ApplicationException {
		customersController.deleteCustomer(customerId);
	}

	@GetMapping("/{customerId}")
	public Customer getCustomer(@PathVariable ("customerId") long customerId) throws ApplicationException {
		return customersController.getCustomer(customerId);
	}
	
	@GetMapping("/allCustomers")
	public List<Customer> getAllCustomers() throws ApplicationException {
		return customersController.getAllCustomers();
	}
	
}
