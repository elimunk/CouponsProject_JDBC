package com.eli.coupons.interfaces;

import java.util.List;

import com.eli.coupons.beans.User;
import com.eli.coupons.enums.ClientType;
import com.eli.coupons.exceptions.ApplicationException;

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
