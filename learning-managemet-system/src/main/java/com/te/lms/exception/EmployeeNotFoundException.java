package com.te.lms.exception;

//By this annotation for ignoring the compiler warning about serialization version
@SuppressWarnings("serial") 
public class EmployeeNotFoundException extends Exception{
	
	public EmployeeNotFoundException(String message) {
		super(message);
	}
	
}
