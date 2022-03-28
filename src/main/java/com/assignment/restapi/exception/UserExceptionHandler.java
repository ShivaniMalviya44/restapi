package com.assignment.restapi.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler{
	   
	    @ExceptionHandler (value = { Exception.class})
		public ResponseEntity<Object> handleUserException(Exception ex, WebRequest request) {	    
		String errorMessageDesc = ex.getLocalizedMessage();
		if(errorMessageDesc == null) errorMessageDesc= ex.toString();
		    ErrorMessage errorMessage = new ErrorMessage(new Date(),errorMessageDesc, request.getDescription(false));
		    return new  ResponseEntity<Object> (errorMessage , HttpStatus.INTERNAL_SERVER_ERROR);
		}
}

