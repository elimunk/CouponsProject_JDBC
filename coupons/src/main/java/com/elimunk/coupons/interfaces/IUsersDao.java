package com.elimunk.coupons.interfaces;

import java.util.List;

import com.elimunk.coupons.beans.User;
import com.elimunk.coupons.enums.ClientType;
import com.elimunk.coupons.exceptions.ApplicationException;

public interface IUsersDao {

	boolean isUserExistById(long userId) throws ApplicationException;

	boolean isUserExistByName(String userName) throws ApplicationException;

	long createUser(User user) throws ApplicationException;

	void updateUser(User user) throws ApplicationException;

	User getUser(long userId) throws ApplicationException;

	User getUser(String userName) throws ApplicationException;

	List<User> getAllUsers() throws ApplicationException;

	void deleteUser(long userId) throws ApplicationException;

	void deleteCompanyUsers(long companyId) throws ApplicationException;

	ClientType login(String userName, String password) throws ApplicationException;


}
