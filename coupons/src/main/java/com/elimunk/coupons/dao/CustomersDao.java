package com.elimunk.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.elimunk.coupons.beans.Customer;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.interfaces.ICustomersDao;
import com.elimunk.coupons.interfaces.IUsersDao;
import com.elimunk.coupons.utils.DateUtils;
import com.elimunk.coupons.utils.JdbcUtils;

@Repository
public class CustomersDao implements ICustomersDao {

	@Override
	public boolean isCustomerExsistByEmail(String email) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM customers WHERE email = ?");
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "The 'is customer exist' query is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}

	@Override
	public boolean isCustomerExsistById(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM customers WHERE customer_id = ?");
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "The 'is customer exist' query is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}


	@Override
	public long addCustomer(Customer customer) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sqlQuery = "INSERT INTO customers(customer_id ,FIRST_NAME, LAST_NAME, EMAIL, Phone_number) VALUES (?,?,?,?,?)";
		long customerId = customer.getUserCustomer().getId();
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery);

			preparedStatement.setLong(1, customerId);
			preparedStatement.setString(2, customer.getFirstName());
			preparedStatement.setString(3, customer.getLastName());
			preparedStatement.setString(4, customer.getEmail());
			preparedStatement.setString(5, customer.getPhoneNumber());

			preparedStatement.executeUpdate();
			customer.setId(customerId);
			return customerId;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to add customer");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	
	@Override
	public void updateCustomer(Customer customer) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sqlQuery = "UPDATE customers SET first_name=?, last_name=?, email=?, Phone_number=?  WHERE customer_id=?";
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery);

			preparedStatement.setString(1, customer.getFirstName());
			preparedStatement.setString(2, customer.getLastName());
			preparedStatement.setString(3, customer.getEmail());
			preparedStatement.setString(4, customer.getPhoneNumber());
			preparedStatement.setLong(5, customer.getId());

			int res = preparedStatement.executeUpdate();
			if (res != 0)
				System.out.println("Customer No " + customer.getId() + " updated successfully");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Update customer failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}


	@Override
	public void deleteCustomer(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM customers WHERE customer_id=?");
			preparedStatement.setLong(1, customerId);
			preparedStatement.executeUpdate();

			System.out.println("Customer No " + customerId + " deleted successfully");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete customer failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}


	@Override
	public long getCustomerIdByEmail(String email) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT customer_id FROM customers WHERE email = ? ");
			preparedStatement.setString(1, email);

			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getLong("ID");
			} else
				throw new ApplicationException(ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to get the Id");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "'Get customer Id' is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}


	@Override
	public Customer getCustomer(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM customers WHERE customer_id= ?");
			preparedStatement.setLong(1, customerId);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				customer = extractCustomerFromResultSet(resultSet);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get customer is failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return customer;
	}


	@Override
	public List<Customer> getAllCustomers() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Customer> allCustomers = new ArrayList<Customer>();

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM customers");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Customer customer = extractCustomerFromResultSet(resultSet);
				allCustomers.add(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get all customers failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return allCustomers;
	}
	
	
	private Customer extractCustomerFromResultSet(ResultSet resultSet) throws ApplicationException {
		IUsersDao userDao = new UsersDao();
		Customer customer = new Customer();
		try {
			customer.setId(resultSet.getLong("customer_id"));
			customer.setFirstName(resultSet.getString("FIRST_NAME"));
			customer.setLastName(resultSet.getString("LAST_NAME"));
			customer.setEmail(resultSet.getString("EMAIL"));
			customer.setPhoneNumber(resultSet.getString("Phone_number"));
			customer.setUserCustomer(userDao.getUser(resultSet.getLong("customer_id")));
			return customer;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to extract customer from ResultSet");
		}
	}
}
