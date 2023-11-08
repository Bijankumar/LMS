package com.te.lms.dto;

import java.sql.Date;
import java.util.List;

import com.te.lms.entity.EmployeeAddressInfo;
import com.te.lms.entity.EmployeeBankDetails;
import com.te.lms.entity.EmployeeContactInfo;
import com.te.lms.entity.EmployeeEducationInfo;
import com.te.lms.entity.EmployeeExperienceInfo;
import com.te.lms.entity.EmployeeSecondaryInfo;
import com.te.lms.entity.EmployeeTechnicalSkillsInfo;
import com.te.lms.entity.MockRating;

import lombok.Data;

@Data
public class EmployeePrimaryInfoDto {
	private int employeeNo;
	private String employeeId;
	private String employeeName;
	private String employeeGender;
	private Date employeeDateOfBirth;
	private Date employeeCurrentDateOfJoining;
	private String employeeEmail; // userName
	private String employeeBloodGroup;
	private String employeeCurrentDesignation;
	private String employeeNationality;
	private String employeeStatus;
	private int employeeAttendance;
	private String employeeRole;
	private String pswResetStatus;
	private String password;

	private EmployeeSecondaryInfo secondaryInfo;

	private List<EmployeeEducationInfo> educationInfo;

	private List<EmployeeTechnicalSkillsInfo> skillsInfo;

	private List<EmployeeExperienceInfo> experienceInfo;

	private List<EmployeeBankDetails> bankDetails;

	private List<EmployeeAddressInfo> addressInfo;

	private List<EmployeeContactInfo> contactInfo;

	private List<MockRating> mockRatings;
}
