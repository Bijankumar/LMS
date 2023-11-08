package com.te.lms.exception;

//By this annotation for ignoring the compiler warning about serialization version
@SuppressWarnings("serial")
public class ApprovalFailedException extends Exception {

	public ApprovalFailedException(String message) {
		super(message);
	}

}
