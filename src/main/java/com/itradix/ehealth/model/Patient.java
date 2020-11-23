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
	
	@Column(name="email")
	private String email;
	
	@Column(name="bloodgroup")
	private String bloodGroup;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="lastnames")
	@ElementCollection(targetClass=String.class)
	private List<String> lastNames;
	

	@Column(name="notes")
	private String message;
	
	public Patient(Date birthDate, String email, String bloodGroup, String firstName, String gender,
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

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBloodGroup() {
		return this.bloodGroup;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder().append(getBirthDate()).append(getEmail())
				.append(getBloodGroup()).append(getGender()).append(getFirstName()).append(getLastNames())
				.append(getMessage());
		return builder.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, Arrays.asList(id.toString()));
	}

}
