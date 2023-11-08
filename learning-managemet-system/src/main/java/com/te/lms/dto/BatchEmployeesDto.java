package com.te.lms.dto;

import java.util.List;

import com.te.lms.entity.MockRating;

import lombok.Data;

@Data
public class BatchEmployeesDto {
	private int employeeNo;
	private String employeeId;
	private String employeeName;
	private int employeeMockTaken;
	private String employeeStatus;
	private int employeeAttendance;
	private List<MockRating> mockRatings;
}
