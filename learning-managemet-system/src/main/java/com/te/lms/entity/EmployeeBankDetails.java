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
@Table(name = "employee_bank_detail")
public class EmployeeBankDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeBankDetailId;
	@NotEmpty(message = "Bank_Name shouldn't be empty")
	private String employeeBankName;
	@NotEmpty(message = "Account_Number shouldn't be empty")
	private String employeeBankAccountNo;
	@NotEmpty(message = "IFSC_Code shouldn't be empty")
	private String employeeBankAccountIfscCode;
	@NotEmpty(message = "Account_Type shouldn't be empty")
	private String employeeBankAccountType;
	@NotEmpty(message = "Barnch_Name shouldn't be empty")
	private String employeeBankAccountBarnchName;
	@NotEmpty(message = "Branch_State shouldn't be empty")
	private String employeeBankAccountBranchState;

}
