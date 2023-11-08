package com.te.lms.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.te.lms.entity.EmployeePrimaryInfo;
import com.te.lms.entity.MentorDetails;
import com.te.lms.repository.LmsEmployeeRepository;
import com.te.lms.repository.LmsMentorRepository;

public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private LmsEmployeeRepository lmsEmployeeRepository;

	@Autowired
	private LmsMentorRepository lmsMentorRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EmployeePrimaryInfo primaryInfo = lmsEmployeeRepository.findByemployeeEmail(username);
		MentorDetails mentor = lmsMentorRepository.findBymentorEmail(username);

		if (primaryInfo != null)
			return new CustomUserDetails(primaryInfo);
		else if (mentor != null)
			return new CustomUserDetails(mentor);
		else
			throw new UsernameNotFoundException("Invalid User Credential");
	}

}
