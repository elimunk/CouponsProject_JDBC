 package com.elimunk.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.elimunk.coupons.beans.Purchase;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.interfaces.ICouponsDao;
import com.elimunk.coupons.interfaces.IPurchasesDao;
import com.elimunk.coupons.utils.DateUtils;
import com.elimunk.coupons.utils.JdbcUtils;

@Repository
public class PurchasesDao implements IPurchasesDao {
	
	ICouponsDao couponsDao = new CouponsDao();
	
	@Override
	public boolean isPurchaseExistById(long purchaseId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM purchases WHERE purchase_id =?");
			preparedStatement.setLong(1, purchaseId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "The 'is purchase exist' query is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}
	@Override
	public long addPurchase(Purchase purchase) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sqlQuery = "INSERT INTO purchases (customer_id , coupon_id, purchase_amount) VALUES (?,?,?)";

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, purchase.getCustomerId());
			preparedStatement.setLong(2, purchase.getCouponId());
			preparedStatement.setLong(3, purchase.getAmount());
			preparedStatement.executeUpdate();

			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				purchase.setId(id);
				return id;
			}
			else {
				throw new ApplicationException(ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to create purchase id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Add coupon purchase failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
	}

	@Override
	public void deletePurchaseById(long purchaseId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM purchases WHERE purchase_id=?");
			preparedStatement.setLong(1, purchaseId);
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
				System.out.println("Purchase " + purchaseId + " deleted successfully.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete coupon purchase failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	@Override
	public void deletePurchaseByCouponId(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
	
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM purchases WHERE coupon_id=?");
			preparedStatement.setLong(1, couponId);
			int res = preparedStatement.executeUpdate();
			if (res != 0)
				System.out.println("Coupon No " + couponId + " deleted from purchases successfully.");
	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete coupon purchase failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	@Override
	public void deleteCompanyPurchases(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sqlQuery = "DELETE FROM purchases WHERE coupon_id IN (SELECT coupon_id FROM coupons WHERE company_id=?)";
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setLong(1, companyId);
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
				System.out.println("All purchases of company No " + companyId + " deleted from purchases successfully.");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete company purchases failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	

	@Override
	public void deleteCustomerPurchases(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM purchases WHERE customer_Id=?");
			preparedStatement.setLong(1, customerId);
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
				System.out.println("All purchases of customer No " + customerId + " deleted successfully.");
			} 
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete customer purchases is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	@Override
	public Purchase getPurchase(long purchaseId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Purchase purchase = null;
	
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM purchases WHERE purchase_id= ?");
			preparedStatement.setLong(1, purchaseId);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				purchase = extactPurchaseFromResultSet(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to get purchase. ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return purchase;
	}
	
	@Override
	public List<Purchase> getAllPurchases() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Purchase> allPurchases = new ArrayList<Purchase>();
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM purchases");
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Purchase purchase = extactPurchaseFromResultSet(resultSet);
				allPurchases.add(purchase);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to get all purchases. ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return allPurchases;
	}
	
	
	private Purchase extactPurchaseFromResultSet(ResultSet resultSet) throws ApplicationException {
		Purchase purchase = new Purchase();
		
		try {
			purchase.setId(resultSet.getLong("purchase_id"));
			purchase.setCustomerId(resultSet.getLong("customer_id"));
			purchase.setCouponId(resultSet.getLong("coupon_id"));
			purchase.setAmount(resultSet.getInt("purchase_amount"));
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to extract purchase from ResultSet");
		}
		return purchase;
	}
		
		
}
