package com.elimunk.coupons.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.annotation.Transactional;

import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.beans.User;
import com.elimunk.coupons.beans.UserIdTypeAndToken;
import com.elimunk.coupons.dao.CompaniesDao;
import com.elimunk.coupons.dao.CustomersDao;
import com.elimunk.coupons.dao.PurchasesDao;
import com.elimunk.coupons.dao.UsersDao;
import com.elimunk.coupons.enums.ClientType;
import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.interfaces.ICacheManager;
import com.elimunk.coupons.utils.DateUtils;
import com.elimunk.coupons.utils.ValidateUtils;

//this is the users logic level to control of all operations of the users 
@Controller
public class UsersController {

	@Autowired
	private ICacheManager cacheManager;
	
// property
	
	// Instances of 'Dao' levels for the users operations 
	@Autowired
	private UsersDao userDao;
	@Autowired
	private CompaniesDao companyDao;
	@Autowired
	private CustomersDao customerDao;
	@Autowired
	private PurchasesDao purchasesDao;
	
	
	public UsersController() {
		super();
	}

//	methods
	
	public long createUser(User user) throws ApplicationException {
		// validate the user before the creating 
		validateUser(user);
		validateUaerNameNotExist(user);
		// if the user is valid - add to the database.
		return userDao.createUser(user);
	}
		
	public void updateUser(User user, PostLoginUserData userData) throws ApplicationException {
		validateUserAccess(user.getId(), userData);
		// Check if the user exists. before the action
		validateExistUser(user.getId());
		// validate the update user before the creating 
		validateUpdateUser(user);
		// if user change the userName, validate that the userName not already taken
		if (!userDao.getUser(user.getId()).getUserName().equals(user.getUserName())) {
			validateUaerNameNotExist(user);
		}
		// Update the user in the database 
		userDao.updateUser(user);
	}
	
	public void deleteUser(long userId, PostLoginUserData userData) throws ApplicationException {
		validateUserAccess(userId, userData);
		// Check if the user exists. before the action
		validateExistUser(userId);
		// if the user is a customer user- delete the purchases and the customer first
		if(userDao.getUser(userId).getType().name()=="CUSTOMER") {
			purchasesDao.deleteCustomerPurchases(userId);
			customerDao.deleteCustomer(userId);
		}
		// delete the user 
		userDao.deleteUser(userId);
	}
	
	public User getUser(long userId) throws ApplicationException {
		// Check if the user exists. before the action
		validateExistUser(userId);
		// Get the user from the database
		return userDao.getUser(userId);
	} 
	
	public List<User> getAllUsers() throws ApplicationException {
		return userDao.getAllUsers();
	}
	
	public List<User> getCompanyUsers(long companyId) throws ApplicationException {
		return userDao.getCompanyUsers(companyId);
	}

	public UserIdTypeAndToken login(String userName, String password) throws ApplicationException {
		// Get the user type if login success
		ClientType clientType = userDao.login(userName, password);
		// generate new token for this user
		String token = generateEncryptedToken(userName);
		// generate new user data for this user
		PostLoginUserData userData = generateUserData(userName);
		// insert into the cache manager the token of the user and the data of the user
		cacheManager.put(token, userData);
		System.out.println("token: " + token+ " , Post Login User Data : " + userData);
		UserIdTypeAndToken userTypeAndToken = new UserIdTypeAndToken (userData.getUserId() , clientType , token , userData.getCompanyId());
		// return the user type
		return userTypeAndToken;
	}
	
	// validate the user before adding 
	private void validateUser(User user) throws ApplicationException {
		// the user name must contains minimum 5 characters and only this characters [ a-z A-Z 0-9 . @ _ - ] allowed
		if (!ValidateUtils.isUserNameValid(user.getUserName())) {
			throw new ApplicationException(ErrorTypes.INVALID_USER_NAME, DateUtils.getCurrentDateAndTime(), "Can't create user, The User contains invalid userName." ,false);
		}
		// password must contains 8 characters only. and atleast one letter and one number. Example 'a1234567'
		if (!ValidateUtils.isPasswordValid(user.getPassword())) {
			throw new ApplicationException(ErrorTypes.INVALID_PASSWORD, DateUtils.getCurrentDateAndTime(), "Can't create user, The User contains invalid passowrd." ,false);
		}
		// if the user is a company user , validate the company is exist
		if (user.getType().name().equals("COMPANY")) {
			if (!companyDao.isCompanyExistById(user.getCompanyId())) {
				throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), 
						"Can't create user, The User contains invalid companyId. this company does not exist." ,false);
			}
		}
	}
	
	// validate the user before adding 
	private void validateUpdateUser(User user) throws ApplicationException {
		// the user name must contains minimum 5 characters and only this characters [ a-z A-Z 0-9 . @ _ - ] allowed
		if (!ValidateUtils.isUserNameValid(user.getUserName())) {
			throw new ApplicationException(ErrorTypes.INVALID_USER_NAME, DateUtils.getCurrentDateAndTime(), "Can't create user, The User contains invalid userName." ,false);
		}
		// password must contains 8 characters only. and atleast one letter and one number. Example 'a1234567'
		if (!ValidateUtils.isPasswordValid(user.getPassword())) {
			throw new ApplicationException(ErrorTypes.INVALID_PASSWORD, DateUtils.getCurrentDateAndTime(), "Can't create user, The User contains invalid passowrd." ,false);
		}
	}
	
	// validate the user userName not already exist .
	private void validateUaerNameNotExist(User user) throws ApplicationException {
		if (userDao.isUserExistByName(user.getUserName())) {
			throw new ApplicationException(ErrorTypes.USER_NAME_EXIST, DateUtils.getCurrentDateAndTime(), "The user name allredy exsist." ,false);
		}
	}
	
	
	// validate that the user exist
	private void validateExistUser(long userId) throws ApplicationException {
		if (!userDao.isUserExistById(userId)) {
			throw new ApplicationException(ErrorTypes.NOT_EXIST, DateUtils.getCurrentDateAndTime(), "This user does not exist." ,false);
		}
	}
	
	// after user login is OK - generate the post login user data 
	private PostLoginUserData generateUserData(String userName) throws ApplicationException {
		User theUser = userDao.getUser(userName);
		PostLoginUserData userData = new PostLoginUserData(theUser.getId(), theUser.getCompanyId(), theUser.getType());
		return userData;
	}
	
	// function for generate a randomly token for the user (requests) 
	private String generateEncryptedToken(String user) {
		String token = "Sugar - junk data and shit" + user + "Sheker kolshehu amen 777" ;
		// like "Encrypting" the token (in fact, hashing isn't really encryption)
		return ""+token.hashCode()+ token.toUpperCase().hashCode();
	}

	// checks that the user have the correct user Id before operation on the user
	private void validateUserAccess(long userId, PostLoginUserData userData) throws ApplicationException {
		// if the user type is not administrator, check that the user id is correct
		if (userData.getClientType().name() != "ADMINISTRATOR") {
			if (userId != userData.getUserId()) {
				// if the user user Id is not correct throw HACKING ERROR
				throw new ApplicationException(ErrorTypes.HACKING_ERROR, DateUtils.getCurrentDateAndTime(),
						" HACKING! user id is not correct!" ,true);
			}
		}
	}
}

