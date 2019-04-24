package com.elimunk.coupons.enums;

public enum ErrorTypes {

GENERAL_ERROR("GENERAL ERROR "),
HACKING_ERROR("You don't have permission!"),
LOGIN_FAILED("Login failed! "),
INVALID_NAME("The name is not valid! only letters are allowed "),
INVALID_USER_NAME("User name Invalid. must contains between 5-40 characters (only a-z or 0-9 or '@._-' are allowed "),
INVALID_EMAIL("Invalid email. must be a regular expression. Example: 'email@email.com'. "),
INVALID_END_DATE("The end date invalid. cannot be earlier than the Start date or current date "),
INVALID_PASSWORD("Password must contains 8 characters only. and atleast one letter and one number. Example 'a1234567' "),
INVALID_PHONE_NUMBER("Phone number must contains digits only and between 7-15 digits. "),
INVALID_TITLE("Title is not valid !must contain 2 - 50 characters! "),
INVALID_DESCRPTION("Descrption must contain minimum 15 characters!"),
INVALID_USER_TYPE("Sorry. this user can't be a customer. "),
INVALID_AMOUNT("You cannot purchase this amount , please check the availble amount. "),
EMAIL_EXIST("This Email address already exist. Please choose a different Email address "),
NAME_EXIST("The name is already exist. Please choose a different Name. "),
TITLE_EXIST("The title of the coupon already exsit "),
USER_NAME_EXIST("The user name already exist "),
NOT_EXIST("NOT FOUND"),
NOT_FOUND("Result not found"),
TOO_SHORT_NAME("The name is too short! must contain minimum 2 characters "),
VALUE_OF_ZERO("Must be a positive number "),
OUT_OF_STOCK("Coupon is no longer available for purchase. ");

	private String internalMessage;

	private ErrorTypes(String message) {
		this.internalMessage = message;
	}

	public String getInternalMessage() {
		return internalMessage;
	}
	
}
