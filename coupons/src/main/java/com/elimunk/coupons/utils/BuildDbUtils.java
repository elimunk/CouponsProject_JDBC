 package com.elimunk.coupons.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.elimunk.coupons.enums.Category;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;

/**
 * This class is used to build database , delete and create the tables in mySql
 * for our project. the class contains a function to build database, and
 * functions to delete and create tables. the class also contains a function
 * {@link#createDatabaseAndTables()} to create our project database if not exist and to create all the tables (for starting
 * test). the class also contains a variables String for each table in our
 * project. also contains a function that inserts a category to categories
 * table. this class is a singleton that contains one instance of the class.
 * 
 * @author Eli Munk
 * @version 1.0
 */
public class BuildDbUtils {
	
    //	-----properties-----

	// the String query for create the companies table
	private final String companiesTableText = "CREATE TABLE IF NOT EXISTS companies ("
			+ "`company_id` BIGINT NOT NULL AUTO_INCREMENT," 
			+ "`name` VARCHAR(50) UNIQUE NOT NULL,"
			+ "`logo` VARCHAR(40) UNIQUE NOT NULL," 
			+ "PRIMARY KEY (`company_id`)" + ")";

	// the String query for create the coupons table
	private final String couponsTableText = "CREATE TABLE IF NOT EXISTS coupons ("
			+ "`coupon_id` BIGINT NOT NULL AUTO_INCREMENT," 
			+ "`company_id` BIGINT NOT NULL,"
			+ "`category` VARCHAR(40) NOT NULL," 
			+ "`title` VARCHAR(40) UNIQUE NOT NULL,"
			+ "`description` VARCHAR(400) NOT NULL," 
			+ "`start_date` DATE NOT NULL DEFAULT '0000-00-00',"
			+ "`end_date`  DATE NOT NULL DEFAULT '0000-00-00'," 
			+ "`amount` INT NOT NULL DEFAULT 0,"
			+ "`price` DOUBLE NOT NULL DEFAULT 0.0 ," 
			+ "`image` VARCHAR(20) NULL," + "PRIMARY KEY (`coupon_id`),"
			+ "FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),"
			+ "FOREIGN KEY (`category`) REFERENCES `categories` (`name`)" + ")";

	// the String query for create the coupons table
	private final String customersTableText = "CREATE TABLE IF NOT EXISTS customers ("
			+ "`customer_id` BIGINT NOT NULL," 
			+ "`First_Name` VARCHAR(20) NOT NULL,"
			+ "`Last_Name` VARCHAR(20) NOT NULL," 
			+ "`Email` VARCHAR(40) UNIQUE NOT NULL,"
			+ "`Phone_number` VARCHAR(20) NOT NULL," 
			+ "PRIMARY KEY (`customer_id`),"
			+ "FOREIGN KEY (`customer_id`) REFERENCES users (`user_id`)" + ")";

	// the String query for create the coupons table
	private final String categoriesTableText = "CREATE TABLE IF NOT EXISTS categories ("
			+ "`category_id` INT NOT NULL AUTO_INCREMENT," 
			+ "`name` VARCHAR(40) UNIQUE NOT NULL,"
			+ " PRIMARY KEY (`category_id`)" + " )";

	// the String query for create the coupons table
	private final String purchasesTableText = "CREATE TABLE IF NOT EXISTS purchases ("
			+ "`purchase_id` BIGINT NOT NULL AUTO_INCREMENT," 
			+ "`customer_id` BIGINT NOT NULL,"
			+ "`coupon_id` BIGINT NOT NULL," 
			+ "`purchase_amount` INT NOT NULL," 
			+ "PRIMARY KEY (`purchase_id`),\n"
			+ "FOREIGN KEY (`customer_id`) REFERENCES customers (`customer_id`),"
			+ "FOREIGN KEY (`coupon_id`) REFERENCES coupons (`coupon_id`)" + ")";
	
	 // the String query for create the users table
	private final String usersTableText = "CREATE TABLE IF NOT EXISTS users ("
			+ "`user_id` BIGINT NOT NULL AUTO_INCREMENT," 
			+ "`user_name` VARCHAR(40) NOT NULL,"
			+ "`password` VARCHAR(40) NOT NULL," 
			+ "`company_id` BIGINT ," 
			+ "`type` VARCHAR(40) NOT NULL,"
			+ "PRIMARY KEY (`user_id`)," 
			+ "FOREIGN KEY (`company_id`) REFERENCES companies (`company_id`)" + ")";

	// methods 
	
	/**
	 * A method for build new database if not exist
	 * @param databaseName is the chosen name of the new database
	 * @throws ApplicationException if there is an accessing/syntax error to the database
	 */
	private void createDatabase(String databaseName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnectionForCreateDatabaseOnly();
			preparedStatement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS " + databaseName);
			int res = preparedStatement.executeUpdate();
			if (res != 0)
				System.out.println("Database '" + databaseName + "' has been created.");
			else
				System.out.println("Database '" + databaseName + "' already exist.");

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Create Data baseed was failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	/**
	 * A method for create new table in the database
	 * @param createTableQuery String query for create table that contains the name, columns and all the properties of the table
	 * @throws ApplicationException if there is an accessing/syntax error to the database
	 */
	private void createTable(String createTableQueryFinal) throws ApplicationException {
		String[] str = createTableQueryFinal.split(" ");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sqlFinal = createTableQueryFinal;

		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement(sqlFinal);
			preparedStatement.executeUpdate();

			System.out.println("table '" + str[5] + "' has been created.");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Create table '" + str[5] + "' failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	/**
	 * A method for delete table from the database if the table exist
	 * @param table is the table name to delete
	 * @throws ApplicationException if there is an accessing/syntax error to the  database
	 */
	private void deleteTable(String table) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtils.getConnection();
			preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS " + table);
			preparedStatement.executeUpdate();
			System.out.println("table '" + table + "' has been deleted.");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "Delete table '" + table + "' failed ");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	/**
	 * Add all the exist categories of coupons (from the enum 'Category') into the
	 * categories table at the database
	 * @throws ApplicationException if there is an accessing/syntax error to the database
	 */
	private void addCategories() throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		String sql = "INSERT INTO categories (name) value (?)";
		
		for (int i = 0; i < Category.values().length; i++) {
			try {
				connection = JdbcUtils.getConnection();
				preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, Category.values()[i].name());
				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), "insert category failed ");
			} finally {
				JdbcUtils.closeResources(connection, preparedStatement);
			}
		}
			System.out.println("All categories have been added to the category table.");
	}

	/**
	 * This method create the database and tables for our project. the method call the
	 * {@link #createDatabase(String)} , {@link #deleteTable(String)} and {@link #createTable(String)} methods, and
	 * use them for delete the tables if exist and create them again. the
	 * 'deleteTable' and 'createTable' methods gets the correct query text for our
	 * tables project from the {@link utils.BuildDbUtils} class attributes
	 * 
	 * @see BuildDbUtils
	 * @throws ApplicationException if there is an accessing/syntax error to the database
	 */
	public void createDatabaseAndTables() throws ApplicationException {
		System.out.println("~~Create dataBase if not exists , Delete and create tables , Add all categories~~\n");
		createDatabase("Coupon_Project");
		deleteTable("purchases");
		deleteTable("coupons");
		deleteTable("categories");
		deleteTable("customers");
		deleteTable("users");
		deleteTable("companies");
		createTable(this.companiesTableText);
		createTable(this.usersTableText);
		createTable(this.customersTableText);
		createTable(this.categoriesTableText);
		createTable(this.couponsTableText);
		createTable(this.purchasesTableText);
		addCategories();
		System.out.println();
	}

}
