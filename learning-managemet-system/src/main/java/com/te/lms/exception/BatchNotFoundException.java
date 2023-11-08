package com.te.lms.exception;

//By this annotation for ignoring the compiler warning about serialization version
@SuppressWarnings("serial")
public class BatchNotFoundException extends Exception{

	public BatchNotFoundException(String message) {
		super(message);
	}

}
