package com.elimunk.coupons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.elimunk.coupons.enums.ErrorTypes;
import com.elimunk.coupons.exceptions.ApplicationException;

public class DateUtils {

	public static boolean isStringDateValid(String dateString) {
		return (dateString.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$"));
	}

	public static Date parseDate(String dateToParse) throws ApplicationException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = null;
		if (!isStringDateValid(dateToParse)) {
			throw new ApplicationException(ErrorTypes.INVALID_END_DATE,	DateUtils.getCurrentDateAndTime(), 
					"Date '" + dateToParse + "'  is not valid! must be in the format \"yyyy-MM-dd\"");
		} else
			try {
				newDate = dateFormat.parse(dateToParse);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ApplicationException(e, ErrorTypes.GENERAL_ERROR, DateUtils.getCurrentDateAndTime(), 
						"parse date failed");
			}
		return newDate;
	}

	public static String stringifyDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String stringShortDate = dateFormat.format(date);
		return stringShortDate;
	}
	
	public static String getCurrentDateAndTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date today = new Date();
		String currentDateAndTime = dateFormat.format(today);

		return currentDateAndTime;
	}
}
