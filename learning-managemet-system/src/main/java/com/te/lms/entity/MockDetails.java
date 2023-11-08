package com.te.lms.entity;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name="mock_details")
public class MockDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mockNo;
	@NotEmpty(message = "Technology cannot be empty")
	private String technology;
	private LocalDateTime mockDateTime;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "mock_takenby_panel_member",joinColumns = {@JoinColumn(name="mock_no")},inverseJoinColumns = {@JoinColumn(name="panel_no")})
	private List<MentorDetails> panel;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private BatchDetails batchDetail;
}
