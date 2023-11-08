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
@Table(name = "employee_experience_info")
public class EmployeeExperienceInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeExperienceDetailNo;
	@NotEmpty(message = "Employee company name is required")
	private String employeeCompanyName;
	@NotEmpty(message = "Employee year of experience is required")
	private String employeeYearOfExperience;
	@NotEmpty(message = "Employee date of joining is required")
	private String employeeDateOfJoining;
	@NotEmpty(message = "Employee date of joining is required")
	private String employeeDateOfRelieving;
	@NotEmpty(message = "Employee designation is required")
	private String employeeDesignation;
	@NotEmpty(message = "Employee company location is required")
	private String employeeCompanyLocation;
}
