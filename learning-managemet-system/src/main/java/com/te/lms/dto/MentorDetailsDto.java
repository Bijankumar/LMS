package com.te.lms.dto;

import java.util.List;

import com.te.lms.entity.EmployeePrimaryInfo;
import com.te.lms.entity.MockDetails;

import lombok.Data;

@Data
public class MentorDetailsDto {
	private int mentorNo;
	private String mentorId;
	private String mentorName;
	private String mentorGender;
	private String mentorEmail;		//userName
	private String mentorSkils;
	private String mentorRole;
	private String pswResetStatus;
	private String password;
	
	private List<EmployeePrimaryInfo> employeeDetail;
	
	private List<MockDetails> mockDetails;
}
