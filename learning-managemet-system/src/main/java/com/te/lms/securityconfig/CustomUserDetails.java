package com.te.lms.securityconfig;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.te.lms.entity.EmployeePrimaryInfo;
import com.te.lms.entity.MentorDetails;

@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails {
	private EmployeePrimaryInfo primaryInfo;
	private MentorDetails mentor;

	public CustomUserDetails(EmployeePrimaryInfo primaryInfo) {
		this.primaryInfo = primaryInfo;
	}

	public CustomUserDetails(MentorDetails mentor) {
		this.mentor = mentor;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
// SimpleGrantedAuthority Stores a String representation of an authority granted to
// the Authentication object.
		
		if (primaryInfo != null) {
			SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(primaryInfo.getEmployeeRole());
			return Arrays.asList(grantedAuthority);
		} else {
			SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(mentor.getMentorRole());
			return Arrays.asList(grantedAuthority);
		}
	}

	@Override
	public String getPassword() {
		if (primaryInfo != null)
			return primaryInfo.getPassword();
		else
			return mentor.getPassword();
	}

	@Override
	public String getUsername() {
		if (primaryInfo != null)
			return primaryInfo.getEmployeeEmail();
		else
			return mentor.getMentorEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
