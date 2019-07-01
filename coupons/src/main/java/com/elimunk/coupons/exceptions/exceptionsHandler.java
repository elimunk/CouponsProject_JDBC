package com.elimunk.coupons.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.elimunk.coupons.enums.ErrorTypes;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@RestControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler()
	public void handleConflict(HttpServletResponse response, Throwable e) throws Throwable {

		if (e instanceof ApplicationException) {

			ApplicationException AppExc = (ApplicationException) e;

			if (AppExc.isCritical() == true) {
				AppExc.printStackTrace();
			}

			if (AppExc.getErrorType().name().equals(ErrorTypes.HACKING_ERROR.name())) {
//				need to send email to the project manager
			}
			response.setHeader("massegeError", AppExc.getErrorType().getInternalMessage());
			response.setStatus(700);
			response.sendError(700, AppExc.getErrorType().getInternalMessage());
		} else {
			e.printStackTrace();
			response.setHeader("massegeError", "System error. something wrong! try again later");
			response.setStatus(705);
			response.sendError(705, "System error. something wrong! try again later");
		}

	}
}
