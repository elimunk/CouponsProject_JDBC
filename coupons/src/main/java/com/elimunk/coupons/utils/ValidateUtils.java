package com.elimunk.coupons.utils;

import com.elimunk.coupons.exceptions.ApplicationException;

public class ValidateUtils {

	public static boolean isNameValid(String name) throws ApplicationException {
		return (name.matches("^(?=.*[a-z])(?=.*[A-Z])[a-z-'A-Z]{2,50}$"));
	}

	public static boolean isEmailValid(String email) throws ApplicationException {
		return (email.matches("^([a-z]|[0-9]|[A-Z]|[_.,])+@([a-z]|[0-9]|[A-Z])+([.])([a-z]|[0-9]|[A-Z]|[.-]){2,}$"));
	}

	public static boolean isPasswordValid(String password) throws ApplicationException {
		return (password.matches("^(?=.*[a-z])(?=.*[0-9])[0-9a-z]{8}$"));
	}

	public static boolean isPhoneNumberValid(String phoneNumber) throws ApplicationException {
		return (phoneNumber.matches("^05[0-9]{8}$"));
	}

	public static boolean isUserNameValid(String userName) throws ApplicationException {
		return (userName.matches("^([a-zA-Z0-9.@_-]){5,40}$"));
	}

	public static boolean isShortText(String text) {
		return (text.length() < 2);
	}
	
}
