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
@Table(name = "employee_secondary_info")
public class EmployeeSecondaryInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeInfoId;
	@NotEmpty(message = "Employee PAN number is required")
	private String employeePanNumber;
	@NotEmpty(message = "Employee Aadhar number is required")
	private String employeeAdharNumber;
	@NotEmpty(message = "Employee father's name is required")
	private String employeeFatherName;
	@NotEmpty(message = "Employee mother's name is required")
	private String employeeMotherName;
	private String employeeSpouseName;
	private String employeePassportNumber;
	@NotEmpty(message = "Employee marital status is required")
	private String employeeMaritalStatus;
}
