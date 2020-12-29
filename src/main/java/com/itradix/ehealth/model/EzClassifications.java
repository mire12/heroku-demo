package com.itradix.ehealth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "ez_classifications")
public class EzClassifications implements Serializable {

	public EzClassifications() {
		
	}
	
	public EzClassifications(String oid, String oidName, String oidVersion, String oidUpdate, String val,
			String caption, String short_id, String description, String updated) {
		super();
		this.oid = oid;
		this.oidName = oidName;
		this.oidVersion = oidVersion;
		this.oidUpdate = oidUpdate;
		this.val = val;
		this.caption = caption;
		this.short_id = short_id;
		this.description = description;
		this.updated = updated;
	}
	
	

	@Column(name = "oid")
	private String oid;

	@Column(name = "oid_name")
	private String oidName;

	@Column(name = "oid_ver")
	private String oidVersion;
	
	@Column(name = "oid_update")
	private String oidUpdate;

	@Id @GeneratedValue
	@Column(name = "val")
	private String val;
	
	@Column(name = "caption")
	private String caption;
	
	@Column(name = "short")
	private String short_id;
	
	@Type(type="text")
	@Column(name = "description")
	private String description;

	@Column(name = "updated")
	private String updated;
	
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOidName() {
		return oidName;
	}

	public void setOidName(String oidName) {
		this.oidName = oidName;
	}

	public String getOidVersion() {
		return oidVersion;
	}

	public void setOidVersion(String oidVersion) {
		this.oidVersion = oidVersion;
	}

	public String getOidUpdate() {
		return oidUpdate;
	}

	public void setOidUpdate(String oidUpdate) {
		this.oidUpdate = oidUpdate;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getShort_id() {
		return short_id;
	}

	public void setShort_id(String short_id) {
		this.short_id = short_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}


	
	
}
