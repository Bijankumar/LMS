package com.te.lms.dto;

import com.te.lms.entity.EmployeePrimaryInfo;

import lombok.Data;

@Data
public class MockRatingDto {
	private int mockInfoNo;
	private String mockType;
	private String mockTakenBy;
	private String mockTechnology;
	private int practicalKnowledge;
	private int theoriticalKnowledge;
	private String overAllFeedBack;
	private String detailedFeedBack;
	private int employeeNo;
	
	private EmployeePrimaryInfo employee;
}
