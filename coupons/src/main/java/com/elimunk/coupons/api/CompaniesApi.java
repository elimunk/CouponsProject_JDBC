package com.elimunk.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elimunk.coupons.beans.Company;
import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.CompaniesController;

@RestController
@RequestMapping("/companies")
public class CompaniesApi {

	@Autowired
	private CompaniesController companiesController; 
	
	@PostMapping
	public void createCompany(@RequestBody Company company , HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		System.out.println();
		companiesController.createCompany(company, userData );
	}
	
	@PutMapping
	public void updateCompany(@RequestBody Company company , HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		companiesController.updateCompany(company, userData);
	}
	
	@DeleteMapping("/{compamyId}")
	public void deleteCompany(@PathVariable("compamyId") long compamyId, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		companiesController.deleteCompany(compamyId, userData);
	}
	
	@GetMapping("/{compamyId}")
	public Company getCompany(@PathVariable("compamyId") long compamyId) throws ApplicationException {
		return companiesController.getCompany(compamyId);
	}
	
	@GetMapping
	public List<Company> getAllCompanies() throws ApplicationException {
		return companiesController.getAllCompanies();
	}
	
}
