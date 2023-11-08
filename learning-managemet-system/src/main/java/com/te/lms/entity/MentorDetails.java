package com.te.lms.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "mentor_details", uniqueConstraints = @UniqueConstraint(columnNames = { "mentorEmail" }))
public class MentorDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mentorNo;
	private String mentorId;
	@NotEmpty(message = "Mentor name cannot be empty")
	private String mentorName;
	@NotEmpty(message = "Mentor Gender cannot be empty")
	private String mentorGender;
	@NotEmpty(message = "Mentor email (username) cannot be empty")
	private String mentorEmail; // userName
	@NotEmpty(message = "Mentor skills cannot be empty")
	private String mentorSkils;
	@NotEmpty(message = "Mentor Role cannot be empty")
	private String mentorRole;
	private String pswResetStatus;
	@NotEmpty(message = "Password cannot be empty")
	@Min(value = 8,message = "Password should be 8 or more")
	private String password;
	
	@OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL)
	private List<BatchDetails> batchDetails;
	
	@ManyToMany(mappedBy = "panel", cascade = CascadeType.ALL)
	private List<MockDetails> mockDetails;
}
