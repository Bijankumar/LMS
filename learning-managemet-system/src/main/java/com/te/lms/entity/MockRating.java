package com.te.lms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name="mock_rating")
@Data
public class MockRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mockInfoNo;
	@NotEmpty(message = "Mock_Type cannot be empty")
	private String mockType;
	@NotEmpty(message = "Panel_Name cannot be empty")
	private String mockTakenBy;
	@NotEmpty(message = "Mock_Technology cannot be empty")
	private String mockTechnology;
	private int practicalKnowledge;
	private int theoriticalKnowledge;
	@NotEmpty(message = "Overall_FeedBack cannot be empty")
	private String overAllFeedBack;
	@NotEmpty(message = "Detail_FeedBack cannot be empty")
	private String detailedFeedBack;
	
}
