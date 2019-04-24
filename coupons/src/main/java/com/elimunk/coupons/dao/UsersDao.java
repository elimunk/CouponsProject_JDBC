package com.elimunk.coupons.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.elimunk.coupons.beans.User;
import com.elimunk.coupons.enums.ClientType;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.interfaces.IUsersDao;
import com.elimunk.coupons.utils.DateUtils;
import com.elimunk.coupons.utils.JdbcUtils;

@Repository
public class UsersDao implements IUsersDao{

	
	@Override
	public boolean isUserExistById(long userId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE user_id =?");
			preparedStatement.setLong(1, userId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "The 'is User Exist' query is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}
	
	@Override
	public boolean isUserExistByName(String userName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE user_name =?");
			preparedStatement.setString(1, userName);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "The 'is User Exist By Name' query is failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}
	
	@Override
	public long createUser(User user) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sqlQuery = "INSERT INTO Users (user_name, password, type, company_id) VALUES(?,?,?,?)";

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getType().name());
			preparedStatement.setBigDecimal(4, (user.getCompanyId() == null) ? null : BigDecimal.valueOf(user.getCompanyId()));
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				long userId = resultSet.getLong(1);
				user.setId(userId);
				System.out.println("User No " + userId + " created successfully");
				return userId;
			} else
				throw new ApplicationException(ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), " Get User id from resutSet was failed");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), " Create User failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	@Override
	public void updateUser(User user) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("UPDATE users SET user_name=?, password=? WHERE user_id=?");
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setLong(3, user.getId());
			int res = preparedStatement.executeUpdate();
			
			if (res != 0) {
				System.out.println("User No " + user.getId() + " updated successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Update user failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	@Override
	public User getUser(long userId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE user_id= ?");
			preparedStatement.setLong(1, userId);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				user = extractUserFromResultSet(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get user is failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return user;
	}
	
	@Override
	public User getUser(String userName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE user_name= ?");
			preparedStatement.setString(1, userName);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				user = extractUserFromResultSet(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get user is failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return user;
	}
	
	@Override
	public List<User> getAllUsers() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<User> allUsers = new ArrayList<User>();
		User user = null;
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM users");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				user = extractUserFromResultSet(resultSet);
				allUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get all users failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return allUsers;
	}
	
	@Override
	public void deleteUser(long userId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM users WHERE user_id=?");
			preparedStatement.setLong(1, userId);
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
			System.out.println("User No " + userId + " deleted successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete user failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	
	@Override
	public void deleteCompanyUsers(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM users WHERE company_id=?");
			preparedStatement.setLong(1, companyId);
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
			System.out.println("Users of company No " + companyId + " deleted successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete company users failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	
	@Override
	public ClientType login(String userName, String password) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE user_name = ? && password = ?");
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new ApplicationException(ErrorTypes.LOGIN_FAILED, DateUtils.getCurrentDateAndTime(), "Login failed ");
			}
			ClientType clientType = ClientType.valueOf(resultSet.getString("type"));
			return clientType;

		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get user has failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	
	private User extractUserFromResultSet(ResultSet resultSet) throws ApplicationException {
		User user = new User();
	
		try {
			user.setId(resultSet.getLong("user_id"));
			user.setUserName(resultSet.getString("user_name"));
			user.setPassword(resultSet.getString("password"));
			user.setCompanyId(resultSet.getLong("company_id"));
			user.setType(ClientType.valueOf(resultSet.getString("type")));
			return user;
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to extract user from ResultSet");
		}
	}

}

