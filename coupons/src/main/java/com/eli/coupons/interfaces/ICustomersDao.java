package com.eli.coupons.interfaces;

import java.util.List;

import com.eli.coupons.beans.Customer;
import com.eli.coupons.exceptions.ApplicationException;


public interface ICustomersDao {

	boolean isCustomerExsistByEmail(String email) throws ApplicationException;

	boolean isCustomerExsistById(long customerId) throws ApplicationException;

	long addCustomer(Customer customer) throws ApplicationException;

	void updateCustomer(Customer customer) throws ApplicationException;

	void deleteCustomer(long customerId) throws ApplicationException;

	long getCustomerIdByEmail(String email) throws ApplicationException;

	Customer getCustomer(long customerId) throws ApplicationException;

	List<Customer> getAllCustomers() throws ApplicationException;

}

