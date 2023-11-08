package com.te.lms.response;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class LmsResponse {
	private boolean error;
	private int status;
	private String message;
	private Object lmsList;
}
