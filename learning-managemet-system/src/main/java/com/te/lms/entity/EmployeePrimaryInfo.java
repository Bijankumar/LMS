package com.te.lms.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "employee_primary_Info", uniqueConstraints = @UniqueConstraint(columnNames = { "employeeEmail" }))
public class EmployeePrimaryInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeNo;
	private String employeeId;
	@NotEmpty(message = "Employee name cannot be empty")
	private String employeeName;
	@NotEmpty(message = "Employee gender cannot be empty")
	private String employeeGender;
	@NotNull(message = "Employee date of birth cannot be null")
	private Date employeeDateOfBirth;
	private Date employeeCurrentDateOfJoining;
	@NotEmpty(message = "Employee email (username) cannot be empty")
	@Email(message = "Invalid email format")
	private String employeeEmail; // userName
	private String employeeBloodGroup;
	private String employeeCurrentDesignation;
	private String employeeNationality;
	private String employeeStatus;
	private int employeeAttendance;
	private String pswResetStatus;
	private String employeeRole;
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	private EmployeeSecondaryInfo secondaryInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_no", referencedColumnName = "employeeNo")
	private List<EmployeeEducationInfo> educationInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_no", referencedColumnName = "employeeNo")
	private List<EmployeeTechnicalSkillsInfo> skillsInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_no", referencedColumnName = "employeeNo")
	private List<EmployeeExperienceInfo> experienceInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_no", referencedColumnName = "employeeNo")
	private List<EmployeeBankDetails> bankDetails;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_no", referencedColumnName = "employeeNo")
	private List<EmployeeAddressInfo> addressInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_no", referencedColumnName = "employeeNo")
	private List<EmployeeContactInfo> contactInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_no", referencedColumnName = "employeeNo")
	private List<MockRating> mockRatings;
}
