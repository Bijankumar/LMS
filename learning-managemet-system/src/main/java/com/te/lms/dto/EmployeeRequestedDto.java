package com.te.lms.dto;

import lombok.Data;

@Data
public class EmployeeRequestedDto {
	private int employeeNo;
	private String employeeId;
	private String employeeName;
	private String employeeYearOfPassOut;
	private String employeePercentage;
	private String employeeYearOfExperience;
	private String employeeConatctNumber;
	private String batchName;
}
