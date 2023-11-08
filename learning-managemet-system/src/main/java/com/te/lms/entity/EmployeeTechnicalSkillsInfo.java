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
@Table(name = "employee_technicalskills_info")
public class EmployeeTechnicalSkillsInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeSkillNo;
	@NotEmpty(message = "Skill_Type cannot be empty")
	private String employeeSkillType;
	@NotEmpty(message = "Skill_Ratings cannot be empty")
	private String employeeSkillRatings;
	private Double employeeYearOfExperienceOverSkill;
}
