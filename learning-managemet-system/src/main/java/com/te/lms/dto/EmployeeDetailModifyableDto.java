package com.te.lms.dto;

import java.sql.Date;
import lombok.Data;

@Data
public class EmployeeDetailModifyableDto {
	//Primary Information
		private String employeeName;
		private String employeeGender;
		private Date employeeDateOfBirth;
		private String employeeEmail; // userName
		private String employeeBloodGroup;
		private String employeeNationality;
		private String password;

	// Secondary Info

		private String employeePanNumber;
		private String employeeAdharNumber;
		private String employeeFatherName;
		private String employeeMotherName;
		private String employeeSpouseName;
		private String employeePassportNumber;
		private String employeeMaritalStatus;

	// Education Info
		private String employeeEducationType;
		private String employeeYearOfPassOut;
		private String employeeUniversityName;
		private String employeeInstituteName;
		private String employeePercentage;
		private String employeeSpecialization;
		private String employeeEducationState;

	// Bank Details
		private String employeeBankName;
		private String employeeBankAccountNo;
		private String employeeBankAccountIfscCode;
		private String employeeBankAccountType;
		private String employeeBankAccountBarnchName;
		private String employeeBankAccountBranchState;

	// Technical Skills
		private String employeeSkillType;
		private String employeeSkillRatings;
		private double employeeYearOfExperinceOverSkill;

	// Address Info
		private String employeeAddressType;
		private String employeeDoorNo;
		private String employeeStreet;
		private String employeeCity;
		private String employeeState;
		private String employeePinCode;
		private String employeeLandMark;

	// Contact Info
		private String employeeConatctType;
		private String employeeConatctNumber;

}
