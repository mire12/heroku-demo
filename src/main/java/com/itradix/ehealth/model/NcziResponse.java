package com.itradix.ehealth.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "ncziresponse")
public class NcziResponse extends BaseEntity implements Serializable {

	public NcziResponse(String doctorID, String evID, String patientID, String signed, String returnMsg, String code,
			String detail, String message, String method) {
		super();
		this.doctorID = doctorID;
		this.evID = evID;
		this.patientID = patientID;
		this.signed = signed;
		this.returnMsg = returnMsg;
		this.code = code;
		this.detail = detail;
		this.message = message;
		this.method = method;
	}
	
	public NcziResponse() {
		
	}

	private static final long serialVersionUID = 1L;

	@Column(name = "did")
	private String doctorID;

	@Column(name = "evid")
	private String evID;

	@Column(name = "pid")
	private String patientID;

	@Column(name = "signed")
	@Type(type="text")
	private String signed;

	@Column(name = "return")
	@Type(type="text")
	private String returnMsg;

	@Column(name = "code")
	private String code;

	@Column(name = "detail")
	@Type(type="text")
	private String detail;

	@Column(name = "message")
	private String message;

	@Column(name = "method")
	private String method;

	public String getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}

	public String getEvID() {
		return evID;
	}

	public void setEvID(String evID) {
		this.evID = evID;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder().append(getDoctorID()).append(getEvID()).append(getPatientID())
				.append(getSigned()).append(getReturnMsg()).append(getCode()).append(getDetail()).append(getMessage())
				.append(getMethod());
		return builder.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, Arrays.asList(id.toString()));
	}

}
