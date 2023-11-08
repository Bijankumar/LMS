package com.te.lms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name="employee_contact_info")
public class EmployeeContactInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeContactDeatilNo;
	@NotEmpty(message = "ConatctType cannot be empty")
	private String employeeConatctType;
	@NotEmpty(message = "Conatct Number cannot be empty")
	@Min(value = 10, message = "ConatctNumber at least 10")
	private String employeeConatctNumber;
}
