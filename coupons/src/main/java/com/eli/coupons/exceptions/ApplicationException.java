package com.eli.coupons.exceptions;

import com.eli.coupons.enums.ErrorTypes;

public class ApplicationException extends Exception{

	private static final long serialVersionUID = 1L;
	
	ErrorTypes errorType;
	String errorCurrentDateAndTime;
	
	public ApplicationException(ErrorTypes errorType, String CurrentDateAndTime, String message) {
		super(CurrentDateAndTime + " " + message);
		this.errorType = errorType;
		this.errorCurrentDateAndTime = CurrentDateAndTime;
	}

	public ApplicationException(Exception exception, ErrorTypes errorType, String CurrentDateAndTime, String message) {
		super(CurrentDateAndTime + " " + message , exception);
		this.errorType = errorType;
	}

	public ErrorTypes getErrorType() {
		return errorType;
	}

	public String getErrorCurrentDateAndTime() {
		return errorCurrentDateAndTime;
	}

}
