package com.itradix.ehealth.model;

import sk.gov.ehealth.common.v1.UserContext;

public class PrevezmiVymennyListok {
	
	private UserContext userContext;
	
	private String externyIDVymennehoListku;
	
	public PrevezmiVymennyListok() {
		// TODO Auto-generated constructor stub
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}

	public String getExternyIDVymennehoListku() {
		return externyIDVymennehoListku;
	}

	public void setExternyIDVymennehoListku(String externyIDVymennehoListku) {
		this.externyIDVymennehoListku = externyIDVymennehoListku;
	}

	

}
