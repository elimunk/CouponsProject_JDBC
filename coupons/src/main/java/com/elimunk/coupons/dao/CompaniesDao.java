package com.elimunk.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.elimunk.coupons.beans.Company;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.interfaces.ICompaniesDao;
import com.elimunk.coupons.utils.DateUtils;
import com.elimunk.coupons.utils.JdbcUtils;

@Repository
public class CompaniesDao implements ICompaniesDao {

	@Override
	public boolean isCompanyExistByName(String name) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM companies WHERE name = ?");
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "The 'is company exist' query is failed ",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}

	
	@Override
	public boolean isCompanyExistById(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM companies WHERE company_id = ?");
			preparedStatement.setLong(1, companyId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "The 'is company exist' query is failed ",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return false;
	}

	
	@Override
	public long addCompany(Company company) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sqlQuery = "INSERT INTO companies(name, logo) VALUES (?,?)";
		
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, company.getName());
			preparedStatement.setString(2, company.getLogo());
			preparedStatement.executeUpdate();

			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				Long id = resultSet.getLong(1);
				company.setId(id);
				return id;
			} else {
				throw new ApplicationException(ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to create purchase id",true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to add company",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	
	@Override
	public void updateCompany(Company company) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("UPDATE companies SET logo=? WHERE company_id=?");

			preparedStatement.setString(1, company.getLogo());
			preparedStatement.setLong(2, company.getId());

			int res = preparedStatement.executeUpdate();
			if (res != 0) {
				System.out.println("Company No " + company.getId() + " updated successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Update company failed ",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	
	@Override
	public void deleteCompany(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM companies WHERE company_id=?");
			preparedStatement.setLong(1, companyId);
			int res = preparedStatement.executeUpdate();
			if (res != 0) {
				System.out.println("Company No " + companyId + " deleted successfully");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete company failed ",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	
	@Override
	public Company getCompany(long companyId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Company company = null;
	
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM companies WHERE company_id= ?");
			preparedStatement.setLong(1, companyId);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				company = extractCompanyFromResultSet(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get company is failed",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return company;
	}


	@Override
	public List<Company> getAllCompanies() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Company> allCompanies = new ArrayList<Company>();
	
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT * FROM companies");
			resultSet = preparedStatement.executeQuery();
	
			while (resultSet.next()) {
				Company company = extractCompanyFromResultSet(resultSet);
				allCompanies.add(company);
			}
			return allCompanies;
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Get all companies failed",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}


	@Override
	public long getCompanyId(String name) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("SELECT id FROM companies WHERE name =?");
			preparedStatement.setString(1, name);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong("ID");
			} else
				throw new ApplicationException(ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to get the Id",true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "'Get company Id' is failed ",true);
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	
	private Company extractCompanyFromResultSet(ResultSet resultSet) throws ApplicationException {
		try {
			Company company = new Company(resultSet.getString("NAME"), resultSet.getString("LOGO"));
			company.setId(resultSet.getLong("company_id"));
			return company;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Failed to extract company from ResultSet",true);
		}
	}

}
