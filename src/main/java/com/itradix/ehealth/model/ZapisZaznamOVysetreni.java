package com.itradix.ehealth.model;

import sk.gov.ehealth.common.v1.UserContext;

public class ZapisZaznamOVysetreni {
	

	private UserContext userContext;

	private String vysetrenieDatetime;
	
	private String odoslanieDatetime;
	
	private String textovyPopis;


	public ZapisZaznamOVysetreni() {
		// TODO Auto-generated constructor stub	
			
	}
	
	public String getTextovyPopis() {
		return textovyPopis;
	}


	public void setTextovyPopis(String textovyPopis) {
		this.textovyPopis = textovyPopis;
	}
	
	
	public String getVysetrenieDatetime() {
		return vysetrenieDatetime;
	}


	public void setVysetrenieDatetime(String vysetrenieDatetime) {
		this.vysetrenieDatetime = vysetrenieDatetime;
	}



	public String getOdoslanieDatetime() {
		return odoslanieDatetime;
	}



	public void setOdoslanieDatetime(String odoslanieDatetime) {
		this.odoslanieDatetime = odoslanieDatetime;
	}	
	

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	
	
}
