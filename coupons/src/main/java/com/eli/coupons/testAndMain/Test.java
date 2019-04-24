package com.eli.coupons.testAndMain;

import java.util.Date;
import java.util.List;

import com.eli.coupons.beans.Company;
import com.eli.coupons.beans.Coupon;
import com.eli.coupons.beans.Customer;
import com.eli.coupons.beans.Purchase;
import com.eli.coupons.beans.User;
import com.eli.coupons.dailyJob.DailyJob;
import com.eli.coupons.enums.Category;
import com.eli.coupons.enums.ClientType;
import com.eli.coupons.exceptions.ApplicationException;
import com.eli.coupons.logic.CompaniesController;
import com.eli.coupons.logic.CouponsController;
import com.eli.coupons.logic.CustomersController;
import com.eli.coupons.logic.PurchasesController;
import com.eli.coupons.logic.UsersController;
import com.eli.coupons.utils.BuildDbUtils;
import com.eli.coupons.utils.DateUtils;

public class Test {

	public static void testAll() throws ApplicationException {

		System.out.println("____________________\n| ~~~ StArT TeSt ~~~ |\n|____________________|\n");
		
		try {
			// create mySql database and the tables for the project
			BuildDbUtils buildDb = new BuildDbUtils();
			buildDb.createDatabaseAndTables();
			
            // start the daily job for for delete expired coupons every 24 hours
			DailyJob job = new DailyJob();
			job.startDailyJob();
			
			// create instances of the controllers for our test
			CompaniesController companyControl = new CompaniesController();
			UsersController userControl = new UsersController();
			CustomersController customerControl = new CustomersController();
			CouponsController couponControl = new CouponsController();
			PurchasesController purchControl = new PurchasesController();

			// create 4 companies
			System.out.println("\n----------  create 4 companies  ----------\n");
			for (int i = 1; i < 5; i++) {
				companyControl.createCompany(new Company("Google" + i, "Logo No " + i));
			}

			// create 1 CUSTOMER user
			System.out.println("\n----------  create 1 CUSTOMER user  ----------\n");
			userControl.createUser( new User("UserBob", "b2345678", ClientType.CUSTOMER));
			
            // create 1 COMPANY user 
			User companyUser = new User("UserAlice", "a2345678", (long) 1, ClientType.COMPANY);
			
			// create 4 customers 
			System.out.println("\n----------  create 4 customers  ----------\n");
			char c = 96;
			for (int i = 0; i < 4; i++) {
				c += 1;
				customerControl.createCustomer(new Customer(c + "-Bob", c + "-Customer", i + "bob@email.com",
						"054000000" + i, new User("userBob" + i, "t123456" + i, ClientType.CUSTOMER)));
			}
			
			// create 5 different coupons
			System.out.println("\n----------  create 5 different coupons  ----------\n");
			for (int i = 1; i < 5; i++) {
				couponControl.createCoupon(new Coupon(1, Category.values()[i], "Title No " + i,
						"description No " + 000 + i, new Date(), DateUtils.parseDate("2019-05-1"+i), 6 + i, 8.5 * i, "Not yet"));
			}
			
			// create 4 purchases
			System.out.println("\n----------  create 4 purchases  ----------\n");
			purchControl.createPurchace(new Purchase(3, 1, 4));
			purchControl.createPurchace(new Purchase(2, 1, 3));
			purchControl.createPurchace(new Purchase(3, 2, 5));
			purchControl.createPurchace(new Purchase(2, 4, 5));
		
			
			// try to create customer with user type company
			System.out.println("\n----------  try to create customer with user type company ----------\n");
			try {
				customerControl.createCustomer(new Customer("o-Alice", "-Company0", "0alice@email.com", "0500000000", companyUser));
			} catch (ApplicationException e) {
				System.out.println(e.getMessage());
			}

			// update (email) customer (id 1)
			System.out.println("\n----------  update (email) customer (id 5)  ----------\n");
			Customer customerToUpdate = customerControl.getCustomer(5);
			customerToUpdate.setEmail("update@email.com");
			customerControl.updateCustomer(customerToUpdate);
	
			// try to update (email) customer (id 1) with an existing email  
			System.out.println("\n----------  try to update (email) customer (id 5) with an existing email  ----------\n");
			try {
			customerToUpdate = customerControl.getCustomer(5);
			customerToUpdate.setEmail("0bob@email.com");
			customerControl.updateCustomer(customerToUpdate);
			} catch (ApplicationException e) {
				System.out.println(e.getMessage());
			}
			
			// update (userNmae) user (id 1)
			System.out.println("\n----------  update (userNmae) user (id 1)  ----------\n");
			User userToUpdate = userControl.getUser(1);
			userToUpdate.setUserName("NewUserName1");
			userControl.updateUser(userToUpdate);
			
			
			// try to update user - invalid userName (to short userName)
			System.out.println("\n----------  try to update user invalid userName  ----------\n");
			try {
				userToUpdate.setUserName("aa");
				userControl.updateUser(userToUpdate);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			
			// try to add a company with an existing name
			System.out.println("\n----------  try to add a company with an existing name  ----------\n");
			try {
				companyControl.createCompany(new Company("google1", "logo no 1"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			
			// try to add a customer - invalid name
			System.out.println("\n----------  try to add a customer - invalid name  ----------\n");
			try {
				customerControl.createCustomer(new Customer("10", "customer", "00bob@email.com", "0547775891",
						new User("UserTest", "b2345678", ClientType.CUSTOMER)));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			
			// try to delete customer does not exist (id-12)
			System.out.println("\n----------  try to delete customer does not exist (id-12) ----------\n");
			try {
				customerControl.deleteCustomer(12);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			
			// try to add coupon with same title
			System.out.println("\n----------  try to add coupon with same title  ----------\n");
			try {
				couponControl.createCoupon(new Coupon(2, Category.FASHION, "Title No 1", "description111 ",
						DateUtils.parseDate("2019-02-28"), DateUtils.parseDate("2020-05-15"), 2, 23, "yet"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			
			// try to add expired coupon
			System.out.println("\n----------  try to add expired coupon  ----------\n");
			try {
				couponControl.createCoupon(new Coupon(2, Category.FASHION, "Title No 150", "description111 ",
						DateUtils.parseDate("2019-02-28"), DateUtils.parseDate("2019-03-15"), 2, 23, "yet"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			
			// Customer try to purchase with purchase Amount 0
			System.out.println("\n----------  Customer try to purchase with purchase Amount 0    ----------\n");
			try {
				purchControl.createPurchace(new Purchase(3, 4, 0));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			
			// Customer try purchase Coupon with coupon amount 0
			System.out.println("\n----------  Customer try purchase Coupon with coupon amount 0  ----------\n");
			try {
			purchControl.createPurchace(new Purchase(3, 1, 3));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			
			// get all companies and print
			System.out.println("\n----------  get all companies and print  ----------\n");
			List<Company> allComapnies = companyControl.getAllCompanies();
			System.out.println(allComapnies);

			// get all customers and print
			System.out.println("\n----------  get all customers and print  ----------\n");
			List<Customer> allCustomers = customerControl.getAllCustomers();
			System.out.println(allCustomers);

			// get all coupons and print
			System.out.println("\n----------  get all coupons and print  ----------\n");
			List<Coupon> allCoupons = couponControl.getAllCoupons();
			System.out.println(allCoupons);

			// get all users and print
			System.out.println("\n----------  get all users and print  ----------\n");
			System.out.println(userControl.getAllUsers());
	
			// get customer coupons and print
			System.out.println("\n----------  get customer (id 2) coupons and print  ----------\n");
			List<Coupon> customerCoupons = couponControl.getCustomerCoupons(2);
			System.out.println(customerCoupons + "\n");

			// get customer coupons by category and print
			System.out.println("\n----------  get customer (id 2) coupons by category (WATCHES) and print  ----------\n");
			customerCoupons = couponControl.getCustomerCouponsByCategory(2, Category.WATCHES);
			System.out.println( customerCoupons + "\n");
			
			// get customer coupons by category and print
			System.out.println("\n----------  get customer (id 2) coupons by max price (30) and print  ----------\n");
			customerCoupons = couponControl.getCustomerCoupons(2);
			customerCoupons = couponControl.getCustomerCouponsByMaxPrice(2, 30);
			System.out.println( customerCoupons + "\n");

			// get company coupons and print
			System.out.println("\n----------  get company (id 1) coupons by max price (20) and print  ----------\n");
			List<Coupon> companyCoupons = couponControl.getCompanyCouponsByMaxPrice(1, 20);
			System.out.println(companyCoupons + "\n");
			
			// get all purchases and print
			System.out.println("\n----------  get all purchases and print  ----------\n");
			System.out.println(purchControl.getAllPurchases() + "\n");
			
			
			// delete coupon
			System.out.println("\n----------  delete a coupon (id 2) ----------\n");
			couponControl.deleteCoupon(2, 1);
			
			try {
			// try to delete coupon with worng company id
			System.out.println("\n----------  try to delete coupon with worng company id ----------\n");
			couponControl.deleteCoupon(3, 2);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			// delete user
			System.out.println("\n----------  delete a user (id 2) ----------\n");
			userControl.deleteUser(2);
			
			// delete company
			System.out.println("\n----------  delete a company (id 1) ----------\n");
			companyControl.deleteCompany(1);
			
			// delete customer
			System.out.println("\n----------  delete a customer (id 5) ----------\n");
			customerControl.deleteCustomer(5);
		
			// user login
			System.out.println("\n----------  user login (id 1)----------\n");
			ClientType clientLoginTest = null;
			clientLoginTest = userControl.login("NewUserName1", "b2345678");
			System.out.println("Is login success: " + (clientLoginTest != null));

			
			// try failed login
			System.out.println("\n----------  try failed login  ----------\n");
			clientLoginTest = null;
			try {
				clientLoginTest = userControl.login("TRY", "0000000");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			System.out.println("Is login success: " + (clientLoginTest != null));

			
			// stop the job
			System.out.println("\n----------  stop the job  ----------\n");
			job.stopDailyJob();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
