package com.te.lms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name="employee_address_info")
public class EmployeeAddressInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeAddressNo; 
	@NotEmpty(message = "AddressType shouldn't be empty")
	private String employeeAddressType;
	@NotEmpty(message = "Door No. shouldn't be empty")
	private String employeeDoorNo;
	@NotEmpty(message = "Street shouldn't be empty")
	private String employeeStreet;
	@NotEmpty(message = "City shouldn't be empty")
	private String employeeCity;
	@NotEmpty(message = "State shouldn't be empty")
	private String employeeState;
	@NotEmpty(message = "PinCode shouldn't be empty")
	@Min(value = 6,message = "PinCode must be 6 digit")
	private String employeePinCode;
	@NotEmpty(message = "Employee LandMark shouldn't be empty")
	private String employeeLandMark;
}
