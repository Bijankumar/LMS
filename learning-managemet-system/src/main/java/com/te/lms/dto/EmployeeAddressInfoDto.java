package com.te.lms.dto;

import lombok.Data;

@Data
public class EmployeeAddressInfoDto {
	private int employeeAddressNo; 
	private String employeeAddressType;
	private String employeeDoorNo;
	private String employeeStreet;
	private String employeeCity;
	private String employeeState;
	private String employeePinCode;
	private String employeeLandMark;
}
