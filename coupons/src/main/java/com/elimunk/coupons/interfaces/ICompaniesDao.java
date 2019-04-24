package com.elimunk.coupons.interfaces;

import java.util.List;

import com.elimunk.coupons.beans.Company;
import com.elimunk.coupons.exceptions.ApplicationException;

public interface ICompaniesDao {

	boolean isCompanyExistById(long companyId) throws ApplicationException;

	boolean isCompanyExistByName(String companyName) throws ApplicationException;

	long addCompany(Company company) throws ApplicationException;

	void updateCompany(Company company) throws ApplicationException;

	void deleteCompany(long companyId) throws ApplicationException;

	Company getCompany(long companyId) throws ApplicationException;

	List<Company> getAllCompanies() throws ApplicationException;

	long getCompanyId(String name) throws ApplicationException;

}
