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
import org.hibernate.annotations.Type;

@Entity
@Table(name="xmlobject")
public class XmlTempObject extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition="TEXT", name="xmlobject")
	private String xmlobject;

	public String getXmlobject() {
		return xmlobject;
	}

	public XmlTempObject(String xmlobject) {
		super();
		this.xmlobject = xmlobject;
	}

	public void setXmlobject(String xmlobject) {
		this.xmlobject = xmlobject;
	}
	
}
