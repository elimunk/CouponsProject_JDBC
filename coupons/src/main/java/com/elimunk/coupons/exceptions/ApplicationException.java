package com.elimunk.coupons.exceptions;

import org.springframework.stereotype.Controller;

import com.elimunk.coupons.enums.ErrorTypes;

@Controller
public class ApplicationException extends Exception{

	private static final long serialVersionUID = 1L;
	private boolean isCritical;
	private ErrorTypes errorType;
	private String errorCurrentDateAndTime;

	
	public ApplicationException() {
	}

	public ApplicationException(ErrorTypes errorType, String CurrentDateAndTime, String message, boolean isCritical) {
		super(CurrentDateAndTime + " " + message);
		this.errorType = errorType;
		this.errorCurrentDateAndTime = CurrentDateAndTime;
		this.isCritical = isCritical;
	}

	public ApplicationException(Exception exception, ErrorTypes errorType, String CurrentDateAndTime, String message, boolean isCritical) {
		super(CurrentDateAndTime + " " + message , exception);
		this.errorType = errorType;
		this.isCritical = isCritical;
	}

	public ErrorTypes getErrorType() {
		return errorType;
	}

	public String getErrorCurrentDateAndTime() {
		return errorCurrentDateAndTime;
	}

	public boolean isCritical() {
		return isCritical;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setErrorType(ErrorTypes errorType) {
		this.errorType = errorType;
	}

	public void setErrorCurrentDateAndTime(String errorCurrentDateAndTime) {
		this.errorCurrentDateAndTime = errorCurrentDateAndTime;
	}
	
	
	
	

}
