package com.itradix.ehealth.model;

import sk.gov.ehealth.common.v1.UserContext;

public class VyhladajZaznamyOvysetreni {
	
	private UserContext userContext;
	
	private String datumOd;

	private String datumDo;
	
	private String citlivost;
	
	private String vlastneZaznamy;
	
	private String kompletnyZaznam;
	
	private String popisMedikacnychZaznamov;


	public VyhladajZaznamyOvysetreni() {
		// TODO Auto-generated constructor stub
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	
	public String getDatumOd() {
		return datumOd;
	}
	
	public void setDatumOd(String datumOd) {
		this.datumOd = datumOd;
	}

	public String getDatumDo() {
		return datumDo;
	}

	public void setDatumDo(String datumDo) {
		this.datumDo = datumDo;
	}

	public String getCitlivost() {
		return citlivost;
	}

	public void setCitlivost(String citlivost) {
		this.citlivost = citlivost;
	}

	public String getVlastneZaznamy() {
		return vlastneZaznamy;
	}

	public void setVlastneZaznamy(String vlastneZaznamy) {
		this.vlastneZaznamy = vlastneZaznamy;
	}

	public String getKompletnyZaznam() {
		return kompletnyZaznam;
	}

	public void setKompletnyZaznam(String kompletnyZaznam) {
		this.kompletnyZaznam = kompletnyZaznam;
	}

	public String getPopisMedikacnychZaznamov() {
		return popisMedikacnychZaznamov;
	}

	public void setPopisMedikacnychZaznamov(String popisMedikacnychZaznamov) {
		this.popisMedikacnychZaznamov = popisMedikacnychZaznamov;
	}
	
}
