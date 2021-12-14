package com.itradix.ehealth.model;

import sk.gov.ehealth.common.v1.UserContext;

public class OverVerziu {

	private UserContext userContext;
	private String date;
	private String oid;
		

	public OverVerziu() {
		// TODO Auto-generated constructor stub
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}


}
