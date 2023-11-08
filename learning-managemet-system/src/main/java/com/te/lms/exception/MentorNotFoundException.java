package com.te.lms.exception;

//By this annotation for ignoring the compiler warning about serialization version
@SuppressWarnings("serial")
public class MentorNotFoundException extends Exception {

	public MentorNotFoundException(String message) {
		super(message);
	}
	
}
