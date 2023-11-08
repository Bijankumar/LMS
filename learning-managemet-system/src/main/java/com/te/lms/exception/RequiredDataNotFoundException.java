package com.te.lms.exception;

//By this annotation for ignoring the compiler warning about serialization version
@SuppressWarnings("serial")
public class RequiredDataNotFoundException extends Exception {

	public RequiredDataNotFoundException(String message) {
		super(message);
	}

}
