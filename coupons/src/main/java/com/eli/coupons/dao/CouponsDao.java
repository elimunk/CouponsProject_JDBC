package com.eli.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eli.coupons.beans.Coupon;
import com.eli.coupons.enums.Category;
import com.eli.coupons.enums.ErrorTypes;
import com.eli.coupons.exceptions.ApplicationException;
import com.eli.coupons.interfaces.ICouponsDao;
import com.eli.coupons.utils.DateUtils;
import com.eli.coupons.utils.JdbcUtils;

@Repository
public class CouponsDao implements ICouponsDao {

	@Override
	public boolean isCouponExistByTitle(String couponTitle) throws ApplicationException {
		// turn on the connections 
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// get the  connection from the connection manager 
			connection = JdbcUtils.getConnection();
			//Combining between the syntax SQL query and our connection
			preparedStatement = connection.prepareStatement("SELECT * FROM COUPONS WHERE title =?");
			//Replacing the question marks in the statement above with the relevant data
			preparedStatement.setString(1, couponTitle);
			//Executing the query
			resultSet = preparedStatement.executeQuery();
			// if we get result return true
			if (resultSet.next()) {
				return true;
			}
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(),  "The 'is coupon exist' query is failed ");
		} finally {
			// after all close all the resources
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		// if no result return false
		return false;
	}
	
	@Override
	public boolean isCouponExistById(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM coupons WHERE coupon_id =?");
			preparedStatement.setLong(1, couponId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(),  "The 'is coupon exist' query is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}


	@Override
	public long addCoupon(Coupon coupon) throws ApplicationException {
		// turn on the connections 
		Connection connection = null;
		// turn on the preparedStatement 
		PreparedStatement preparedStatement = null;
		// turn on the ResultSet 
		ResultSet resultSet = null;
		// The SQL query
		String sqlQuery = "INSERT INTO coupons (company_id, category, title, description ,"
				+ "start_date , end_date , amount , price , image) VALUES (?,?,?,?,?,?,?,?,?)";
		try {
			connection = JdbcUtils.getConnection();
			//Replacing the question marks in the statement above with the relevant data , and define the PreparedStatement to return the key that generated in the database
			preparedStatement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, coupon.getCompanyId());
			preparedStatement.setString(2, coupon.getCategory().name());
			preparedStatement.setString(3, coupon.getTitle());
			preparedStatement.setString(4, coupon.getDescription());
			preparedStatement.setDate(5, new java.sql.Date(coupon.getStartDate().getTime()));
			preparedStatement.setDate(6, new java.sql.Date(coupon.getEndDate().getTime()));
			preparedStatement.setInt(7, coupon.getAmount());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImage());

			preparedStatement.executeUpdate();
			// ResultSet get the key that generated in the database and ruternd by the PreparedStatement
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				// get the key from ResultSet
				long id = resultSet.getLong(1);
				// Set the coupon id and return the id
				coupon.setId(id);
				return id;
			} else {
				throw new ApplicationException(ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to create purchase id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to add Coupon");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// only description, end date , amount, price and image can be changed
	@Override
	public void updateCoupon(Coupon coupon) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sqlQuery = "UPDATE coupons SET description=?, end_date=?, amount=?, price=?, image=? WHERE coupon_id=? ";
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, coupon.getDescription());
			preparedStatement.setDate(2, new java.sql.Date(coupon.getEndDate().getTime()));
			preparedStatement.setInt(3, coupon.getAmount());
			preparedStatement.setDouble(4, coupon.getPrice());
			preparedStatement.setString(5, coupon.getImage());
			preparedStatement.setLong(6, coupon.getId());

			// 	Executing the Update and if update success keep the return value from the preparedStatement
			int res = preparedStatement.executeUpdate();
			if (res != 0)
				System.out.println("Coupon No " + coupon.getId() + " updated successfully");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Update coupon failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// update the coupon amount after purchase
	@Override
	public void uptateCouponAmount(long couponId , int amount) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("UPDATE coupons SET amount=amount-? WHERE coupon_id=?");
			preparedStatement.setInt(1, amount);
			preparedStatement.setLong(2, couponId);
			
			int res = preparedStatement.executeUpdate();
			if (res != 0)
				System.out.println("Coupon quantity No " + couponId + " updated successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Update coupon failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	@Override
	public void deleteCoupon(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM coupons WHERE coupon_id=?");
			preparedStatement.setLong(1, couponId);
			
			int res = preparedStatement.executeUpdate();
			if (res != 0)
				System.out.println("coupon No " + couponId + " deleted successfully.");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete coupon failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	
	public void deleteCompanyCoupons(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM coupons WHERE company_id=?");
			preparedStatement.setLong(1, companyId);
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
				System.out.println("All coupons of company No " + companyId + " deleted successfully.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete company coupon is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void deleteExpiredCoupons() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM coupons WHERE end_date < CURDATE()");
			int res = preparedStatement.executeUpdate();
			
			// if update effected
			if (res != 0) {
				// get the amount of row effected (coupons deleted in this case) 
				int deletedAmount = preparedStatement.getUpdateCount();
				System.out.println(deletedAmount + " expired coupons deleted successfully.");
			}else
				System.out.println("No expired coupons found");	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete coupon failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
		
	}

	@Override
	public List<Coupon> getAllCoupons() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Coupon> allCoupons = new ArrayList<Coupon>();
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM coupons");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				allCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get all coupons failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return allCoupons;
	}

	@Override
	public Coupon getCoupon(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Coupon coupon = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM coupons WHERE coupon_id=?");
			preparedStatement.setLong(1, couponId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				coupon = extractCouponFromResultSet(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get one coupon failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return coupon;
	}
	
	@Override
	public List<Coupon> getCompanyCoupons(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Coupon> companyCoupons = new ArrayList<Coupon>();
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM coupons WHERE company_id=?");
			preparedStatement.setLong(1, companyId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				companyCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get company coupons failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return companyCoupons;
	}
	
	@Override
	public List<Coupon> getCompanyCouponsByCategory(long companyId, Category category) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Coupon> customerCoupons = new ArrayList<Coupon>();
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM coupons WHERE company_id=? AND category=?");
			preparedStatement.setLong(1, companyId);
			preparedStatement.setString(2, category.name());
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				customerCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get company coupons failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return customerCoupons;
	}
	
	@Override
	public List<Coupon> getCompanyCouponsByMaxPrice(long companyId, double maxPrice) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Coupon> customerCoupons = new ArrayList<Coupon>();

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM coupons WHERE company_id=? AND price <=?");
			preparedStatement.setLong(1, companyId);
			preparedStatement.setDouble(2, maxPrice);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				customerCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get company coupons failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return customerCoupons;
	}
	
	@Override
	public List<Coupon> getCustomerCoupons(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Coupon> customerCoupons = new ArrayList<Coupon>();
		String sqlQuery = "SELECT * FROM coupons WHERE coupon_id IN (SELECT coupon_id FROM purchases WHERE customer_id=?)";
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setLong(1, customerId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				customerCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get customer coupons failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return customerCoupons;
	}
	
	@Override
	public List<Coupon> getCustomerCouponsByCategory(long customerId, Category category) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Coupon> customerCoupons = new ArrayList<Coupon>();
		String sqlQuery = "SELECT * FROM coupons WHERE coupon_id IN (SELECT coupon_id FROM purchases WHERE customer_id=?) AND category=?";

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setLong(1, customerId);
			preparedStatement.setString(2, category.name());
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				customerCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get customer coupons failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return customerCoupons;
			}
	
	@Override
	public List<Coupon> getCustomerCouponsByMaxPrice(long customerId, double maxPrice) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Coupon> customerCoupons = new ArrayList<Coupon>();
		String sqlQuery = "SELECT * FROM coupons WHERE coupon_id IN (SELECT coupon_id FROM purchases WHERE customer_id=?) AND price <=?";

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setLong(1, customerId);
			preparedStatement.setDouble(2, maxPrice);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				customerCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get customer coupons failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return customerCoupons;
	}
	
	
	private Coupon extractCouponFromResultSet(ResultSet resultSet) throws ApplicationException {
		Coupon coupon = new Coupon();
		try {
			coupon.setId(resultSet.getLong("coupon_id"));
			coupon.setCompanyId(resultSet.getInt("company_id"));
			coupon.setCategory(Category.valueOf(resultSet.getString("category")));
			coupon.setTitle(resultSet.getString("title"));
			coupon.setDescription(resultSet.getString("description"));
			coupon.setStartDate(resultSet.getDate("start_date"));
			coupon.setEndDate(resultSet.getDate("end_date"));
			coupon.setAmount(resultSet.getInt("amount"));
			coupon.setPrice(resultSet.getDouble("price"));
			coupon.setImage(resultSet.getString("image"));
			return coupon;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to extract coupon from ResultSet");
		}
	}

}
