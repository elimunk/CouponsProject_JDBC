package com.eli.coupons.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.eli.coupons.beans.Company;
import com.eli.coupons.dao.CompaniesDao;
import com.eli.coupons.dao.CouponsDao;
import com.eli.coupons.dao.PurchasesDao;
import com.eli.coupons.dao.UsersDao;
import com.eli.coupons.enums.ErrorTypes;
import com.eli.coupons.exceptions.ApplicationException;
import com.eli.coupons.utils.DateUtils;
import com.eli.coupons.utils.ValidateUtils;

//this is the companies logic level to control of all operations of the companies 
@Controller
public class CompaniesController {

// property
	
	// Instances of 'CompaniesDao' level for the company operations 
	@Autowired
	private CompaniesDao companyDao = new CompaniesDao();

	
	public CompaniesController() {
		super();
	}

//	methods
	
	public void createCompany(Company company) throws ApplicationException {
		// first we confirm that the company is valid to create
		validateCompany(company);
		// if the company is valid - add to the database.
		companyDao.addCompany(company);
		System.out.println("Company No " + company.getId() + " created successfully");
	}
	
	public void updateCompany(Company company) throws ApplicationException {
		// Check if the company exists. before the action
		validateExistCompany(company.getId());
		// Update the company in the database 
		companyDao.updateCompany(company);
	}

	public void deleteCompany(long companyId) throws ApplicationException {
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
		// Check if the company exists. before the action
		validateExistCompany(companyId);
		// Get the coupon from the database
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
					"Add company Failed. this Name already taken. " + "Please choose a different Name");
		}
		// the company name cannot be under 2 characters
		if (ValidateUtils.isShortText(company.getName())) {
			throw new ApplicationException(ErrorTypes.TOO_SHORT_NAME, DateUtils.getCurrentDateAndTime(),
					"Name '" + company.getName() + "' is not valid! must contain minimum 2 characters ");
		}
	}

	// validate that the company exist
	private void validateExistCompany(long companyId) throws ApplicationException {
		if (!companyDao.isCompanyExistById(companyId)) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(),
					"This company does not exist.");
		}
	}
}
