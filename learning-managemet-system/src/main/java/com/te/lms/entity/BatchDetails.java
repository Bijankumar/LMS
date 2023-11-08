package com.te.lms.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "batch_details")
public class BatchDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int batchNo;
	private String batchId;
	@NotBlank(message = "Batch name shouldn't be Blank")
	private String batchName;
	@NotEmpty(message = "Batch Technology shouldn't be empty")
	private String batchTechnology;
	@NotBlank(message = "Batch Start date shouldn't be Blank")
	private Date batchStartDate;
	private Date batchEndDate;
	private int batchStrength;
	@NotEmpty(message = "Batch status shouldn't be empty")
	private String batchStatus;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "batch_no", referencedColumnName = "batchNo")
	private List<EmployeePrimaryInfo> employeeInfo;

	@ManyToOne(cascade = CascadeType.ALL)
	private MentorDetails mentor;

	@OneToMany(mappedBy = "batchDetail", cascade = CascadeType.ALL)
	private List<MockDetails> mocks;
}
