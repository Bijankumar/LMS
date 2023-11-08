package com.te.lms.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.te.lms.entity.BatchDetails;
import com.te.lms.entity.MentorDetails;

import lombok.Data;

@Data
public class MockDetailsDto {
	private int mockNo;
	private String technology;
	private LocalDateTime mockDateTime;
	private int batchNo;
	private List<Integer> mentorsNo;
	
	private List<MentorDetails> panel;
	
	private BatchDetails batchDetail;

}
