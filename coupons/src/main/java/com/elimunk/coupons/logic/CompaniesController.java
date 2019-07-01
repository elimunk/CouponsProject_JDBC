package com.elimunk.coupons.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.elimunk.coupons.beans.Company;
import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.dao.CompaniesDao;
import com.elimunk.coupons.dao.CouponsDao;
import com.elimunk.coupons.dao.PurchasesDao;
import com.elimunk.coupons.dao.UsersDao;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.utils.DateUtils;
import com.elimunk.coupons.utils.ValidateUtils;

//this is the companies logic level to control of all operations of the companies 
@Controller
public class CompaniesController {
	
	// Instances of 'CompaniesDao' level for the company operations 
	@Autowired
	private CompaniesDao companyDao;

// property

	public CompaniesController() {
		super();
	}

//	methods
	
	public void createCompany(Company company, PostLoginUserData userData) throws ApplicationException {
		validateUserAccess(userData);
		// first we confirm that the company is valid to create
		validateCompany(company);
		// if the company is valid - add to the database.
		companyDao.addCompany(company);
		System.out.println("Company No " + company.getId() + " created successfully");
	}
	
	public void updateCompany(Company company, PostLoginUserData userData) throws ApplicationException {
		validateUserAccess(userData);
		// Check if the company exists. before the action
		validateExistCompany(company.getId());
		// Update the company in the database 
		companyDao.updateCompany(company);
	}

	public void deleteCompany(long companyId, PostLoginUserData userData) throws ApplicationException {
		validateUserAccess(userData);
		// Check if the company exists. before the action
		validateExistCompany(companyId);
		
		// after the checks, create instances of 'dao' levels for the next operations 
		CouponsDao couponsDao = new CouponsDao();
		PurchasesDao purchasesDao = new PurchasesDao();
		UsersDao userDao = new UsersDao();
		
		// delete the company purchases, coupons, and users, and then delete the company
		purchasesDao.deleteCompanyPurchases(companyId);
		couponsDao.deleteCompanyCoupons(companyId);
		userDao.deleteCompanyUsers(companyId);
		companyDao.deleteCompany(companyId);
	}

	public Company getCompany(long companyId) throws ApplicationException {
		validateExistCompany(companyId);
		return companyDao.getCompany(companyId);
	}
	
	public List<Company> getAllCompanies() throws ApplicationException {
		List<Company> allCompanies = companyDao.getAllCompanies();
		return allCompanies;
	}
	
	
	// validate the company before adding or updating .
	private void validateCompany(Company company) throws ApplicationException {
		// the company name cannot be taken
		if (companyDao.isCompanyExistByName(company.getName())) {
			throw new ApplicationException(ErrorTypes.NAME_EXIST, DateUtils.getCurrentDateAndTime(),
					"Add company Failed. this Name already taken. " + "Please choose a different Name",false);
		}
		// the company name cannot be under 2 characters
		if (ValidateUtils.isShortText(company.getName())) {
			throw new ApplicationException(ErrorTypes.TOO_SHORT_NAME, DateUtils.getCurrentDateAndTime(),
					"Name '" + company.getName() + "' is not valid! must contain minimum 2 characters ",false);
		}
	}

	// validate that the company exist
	private void validateExistCompany(long companyId) throws ApplicationException {
		if (!companyDao.isCompanyExistById(companyId)) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(),
					"This company does not exist.",false);
		}
	}
	
	// checks that the user have the access before  operation on the company
	private void validateUserAccess(PostLoginUserData userData) throws ApplicationException {
		// if the user type is not administrator throw Hacking alert
		if (userData.getClientType().name() != "ADMINISTRATOR") {
			throw new ApplicationException(ErrorTypes.HACKING_ERROR, DateUtils.getCurrentDateAndTime(),
					" HACKING! company id is not correct!", true);
		}
	}
}
