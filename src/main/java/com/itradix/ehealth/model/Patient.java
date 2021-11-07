package com.itradix.ehealth.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="patient")
public class Patient extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="birthdate")
	private Date birthDate;
	
	@Column(name="birthnumber")
	private String birthNumber;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="lastnames")
	@ElementCollection(targetClass=String.class)
	private List<String> lastNames;
	
	@Column(name="jruzid")
	private String jruzId;
	
	@Column(name="doctorprzs")
	private String doctorPrZs;
	
	@Column(name="insurance")
	private String insurance;
	
	public Patient(Date birthDate, String birthNumber, String firstName, String gender,
			List<String> lastNames, String doctorPrZs, String jruzId, String insurance) {
		super();
		this.birthDate = birthDate;
		this.birthNumber = birthNumber;
		this.firstName = firstName;
		this.gender = gender;
		this.lastNames = lastNames;
		this.doctorPrZs = doctorPrZs;
		this.jruzId = jruzId;
		this.insurance = insurance;
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

	public Date getBirthDate() {
		return this.birthDate;
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
	
	public List<String> getLastNames() {
		return lastNames;
	}

	public void setLastNames(List<String> lastNames) {
		this.lastNames = lastNames;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder().append(getBirthDate()).append(getBirthNumber())
				.append(getJruzId()).append(getGender()).append(getFirstName()).append(getLastNames())
				.append(getInsurance()).append(getDoctorPrZs());
		return builder.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, Arrays.asList(id.toString()));
	}

}
