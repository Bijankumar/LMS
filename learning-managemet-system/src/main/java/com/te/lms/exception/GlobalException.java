package com.te.lms.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> employeeNotFoundException(EmployeeNotFoundException employeeNotFoundException){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeNotFoundException.getMessage());
	}
	
	@ExceptionHandler(BatchNotFoundException.class)
	public ResponseEntity<String> batchNotFoundException(BatchNotFoundException batchNotFoundException){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(batchNotFoundException.getMessage());
	}
	@ExceptionHandler(MentorNotFoundException.class)
	public ResponseEntity<String> mentorNotFoundException(MentorNotFoundException mentorNotFoundException){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mentorNotFoundException.getMessage());
	}
	
	@ExceptionHandler(RequiredDataNotFoundException.class)
	public ResponseEntity<String> requiredDataNotFoundException(RequiredDataNotFoundException dataNotFoundException){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dataNotFoundException.getMessage());
	}
	
	@ExceptionHandler(BatchCreationException.class)
	public ResponseEntity<String> batchCreationException(BatchCreationException creationException){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(creationException.getMessage());
	}
	
	@ExceptionHandler(ApprovalFailedException.class)
	public ResponseEntity<String> approvalFailedException(ApprovalFailedException approvalFailedException){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(approvalFailedException.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handelInvalidArguments(MethodArgumentNotValidException ex){
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error->{
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}
}
