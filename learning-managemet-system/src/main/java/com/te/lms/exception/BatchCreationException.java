package com.te.lms.exception;

//By this annotation for ignoring the compiler warning about serialization version
@SuppressWarnings("serial")
public class BatchCreationException extends Exception {

	public BatchCreationException(String message) {
		super(message);
	}

}
