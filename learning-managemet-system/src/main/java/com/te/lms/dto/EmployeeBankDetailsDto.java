package com.te.lms.dto;

import lombok.Data;

@Data
public class EmployeeBankDetailsDto {
	private int employeeBankDetailId;
	private  String employeeBankName;
	private String employeeBankAccountNo;
	private String employeeBankAccountIfscCode;
	private String employeeBankAccountType;
	private String employeeBankAccountBarnchName;
	private String employeeBankAccountBranchState;
	
}