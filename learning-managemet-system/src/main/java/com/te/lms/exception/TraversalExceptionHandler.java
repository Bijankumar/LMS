package com.te.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

//By this annotation for ignoring the compiler warning about serialization version
@SuppressWarnings("serial")
@Component
public class TraversalExceptionHandler extends RuntimeException {

	public ResponseEntity<String> handelException(Exception e) {
		 String errorMessage = "An error occured"+e.getMessage();
		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
