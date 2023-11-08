package com.te.lms.securityconfig;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class AutoGeneratePassword {
	Random random  = new Random();
	// Auto Password Generation
	
	public String generatePassword(int length) {
	final String characters = "ASDFGHJKLZXCVBNMPOIUYTREWQzxcvbnmlkjhgfdsaqwertyuiop1234567890@#$%^&*()_+-=<>!";
		String generatedPsw="";
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			generatedPsw+=characters.charAt(index);
		}
		return generatedPsw;
	}
	
	// Unique_Id Generation
	
	public String generateId(int length) {
		final String characters = "0123456789";
		String generateId="";
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			generateId+=characters.charAt(index);
		}
		return generateId;
	}
	
}
