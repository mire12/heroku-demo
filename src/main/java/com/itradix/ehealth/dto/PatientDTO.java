package com.itradix.ehealth.dto;

import java.util.Date;
import java.util.List;

public class PatientDTO {
	
	public PatientDTO() {
	}
	
	public PatientDTO(Date birthDate, String birthNumber, String firstName, String gender,
			List<String> lastNames, String jruzId, String doctorPrZs, String insurance) {
		super();
		this.birthDate = birthDate;
		this.birthNumber = birthNumber;
		this.firstName = firstName;
		this.gender = gender;
		this.lastNames = lastNames;
		this.doctorPrZs = doctorPrZs;
	}

	public String getBirthNumber() {
		return birthNumber;
	}

	public void setBirthNumber(String birthNumber) {
		this.birthNumber = birthNumber;
	}

	public String getJruzId() {
		return jruzId;
	}

	public void setJruzId(String jruzId) {
		this.jruzId = jruzId;
	}

	public String getDoctorPrZs() {
		return doctorPrZs;
	}

	public void setDoctorPrZs(String doctorPrZs) {
		this.doctorPrZs = doctorPrZs;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	private Date birthDate;
	private String birthNumber;
	private String firstName;
	private String gender;
	private List<String> lastNames;
	private String jruzId;
	private String doctorPrZs;
	private String insurance;
	
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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

}