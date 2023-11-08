package com.te.lms.dto;

import java.sql.Date;
import java.util.List;

import com.te.lms.entity.BatchDetails;
import com.te.lms.entity.EmployeeAddressInfo;
import com.te.lms.entity.EmployeeBankDetails;
import com.te.lms.entity.EmployeeContactInfo;
import com.te.lms.entity.EmployeeEducationInfo;
import com.te.lms.entity.EmployeeExperienceInfo;
import com.te.lms.entity.EmployeeSecondaryInfo;
import com.te.lms.entity.EmployeeTechnicalSkillsInfo;
import com.te.lms.entity.MentorDetails;
import com.te.lms.entity.MockDetails;
import com.te.lms.entity.MockRating;

import lombok.Data;

@Data
public class EmployeeProfileDto {

//Primary Information
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
	private int employeeMockTaken;
	private int employeeAttendance;
	private String employeeRole;
	private String password;

	private EmployeeSecondaryInfo secondaryInfo;

	private List<EmployeeEducationInfo> educationInfo;

	private List<EmployeeTechnicalSkillsInfo> skillsInfo;

	private List<EmployeeExperienceInfo> experienceInfo;

	private List<EmployeeBankDetails> bankDetails;

	private List<EmployeeAddressInfo> addressInfo;

	private List<EmployeeContactInfo> contactInfo;

	private MentorDetails mentor;

	private BatchDetails batch;

	private List<MockDetails> mock;

	private List<MockRating> mockRatings;

// Secondary Info

	private int employeeInfoId;
	private String employeePanNumber;
	private String employeeAdharNumber;
	private String employeeFatherName;
	private String employeeMotherName;
	private String employeeSpouseName;
	private String employeePassportNumber;
	private String employeeMaritalStatus;

// Education Info
	private int employeeEducationInfoNo;
	private String employeeEducationType;
	private String employeeYearOfPassOut;
	private String employeeUniversityName;
	private String employeeInstituteName;
	private String employeePercentage;
	private String employeeSpecialization;
	private String employeeEducationState;

// Experience Info
	private int employeeExperienceDetailNo;
	private String employeeCompnyName;
	private String employeeYearOfExperience;
	private String employeeDateOfJoining;
	private String employeeDateOfRelieving;
	private String employeeDesignation;
	private String employeeCompanyLocation;

// Bank Details
	private int employeeBankDetailId;
	private String employeeBankName;
	private String employeeBankAccountNo;
	private String employeeBankAccountIfscCode;
	private String employeeBankAccountType;
	private String employeeBankAccountBarnchName;
	private String employeeBankAccountBranchState;

// Technical Skills
	private int employeeSkillNo;
	private String employeeSkillType;
	private String employeeSkillRatings;
	private double employeeYearOfExperinceOverSkill;

// Address Info
	private int employeeAddressNo; 
	private String employeeAddressType;
	private String employeeDoorNo;
	private String employeeStreet;
	private String employeeCity;
	private String employeeState;
	private String employeePinCode;
	private String employeeLandMark;

// Contact Info
	private int employeeContactDeatilNo;
	private String employeeConatctType;
	private String employeeConatctNumber;

}
