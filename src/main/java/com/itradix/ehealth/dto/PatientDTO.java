package com.itradix.ehealth.dto;

import java.util.Date;
import java.util.List;

public class PatientDTO {
	
	public PatientDTO() {
	}
	
	public PatientDTO(Date birthDate, String email, String bloodGroup, String firstName, String gender,
			List<String> lastNames, String message) {
		super();
		this.birthDate = birthDate;
		this.email = email;
		this.bloodGroup = bloodGroup;
		this.firstName = firstName;
		this.gender = gender;
		this.lastNames = lastNames;
		this.message = message;
	}

	private Date birthDate;
	private String email;
	private String bloodGroup;
	private String firstName;
	private String gender;
	private List<String> lastNames;
	private String message;
	
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getLastNames() {
		return lastNames;
	}

	public void setLastNames(List<String> lastNames) {
		this.lastNames = lastNames;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}