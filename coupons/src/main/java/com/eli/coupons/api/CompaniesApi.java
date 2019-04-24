package com.eli.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eli.coupons.beans.Company;
import com.eli.coupons.exceptions.ApplicationException;
import com.eli.coupons.logic.CompaniesController;

@RestController
@RequestMapping("/companies")
public class CompaniesApi {

	@Autowired
	private CompaniesController companiesController; 
	
	@PostMapping
	public void createCompany(@RequestBody Company company) throws ApplicationException {
		companiesController.createCompany(company);
		System.out.println(company);
	}
	
	@PutMapping
	public void updateCompany(@RequestBody Company company) throws ApplicationException {
		companiesController.updateCompany(company);
	}
	
	@DeleteMapping("/{compamyId}")
	public void deleteCompany(@PathVariable("compamyId") long companyId) throws ApplicationException {
		companiesController.deleteCompany(companyId);
	}
	
	@GetMapping("/{compamyId}")
	public Company getCompany(@PathVariable("compamyId") long companyId) throws ApplicationException {
		return companiesController.getCompany(companyId);
	}
	
	@GetMapping("/allCompanies")
	public List<Company> getAllCompanies() throws ApplicationException {
		return companiesController.getAllCompanies();
	}
	
}
