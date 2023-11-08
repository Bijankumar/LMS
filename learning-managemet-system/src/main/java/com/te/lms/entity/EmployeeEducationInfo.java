package com.te.lms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name="employee_education_info")
public class EmployeeEducationInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeEducationInfoNo;
	@NotEmpty(message = "Education_Type cannot be empty")
	private String employeeEducationType;
	@NotEmpty(message = "Year_Of_PassOut cannot be empty")
	private String employeeYearOfPassOut;
	@NotEmpty(message = "University_Name cannot be empty")
	private String employeeUniversityName;
	@NotEmpty(message = "Institute_Name cannot be empty")
	private String employeeInstituteName;
	@NotEmpty(message = "Percentage cannot be empty")
	private String employeePercentage;
	@NotEmpty(message = "Specialization cannot be empty")
	private String employeeSpecialization;
	@NotEmpty(message = "State cannot be empty")
	private String employeeEducationState;
}
