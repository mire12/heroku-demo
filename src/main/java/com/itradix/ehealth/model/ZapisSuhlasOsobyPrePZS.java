package com.itradix.ehealth.model;

import sk.gov.ehealth.common.v1.UserContext;

public class ZapisSuhlasOsobyPrePZS {
	
	private UserContext userContext;
	
	private String citlivost;
	
	private String  platnyDo;
	
	private String  platnyOd;
	
	private String  poznamka;
	
	private String  podpisanaDohoda;
    
	private String  typSuhlasu;
    
	public ZapisSuhlasOsobyPrePZS() {
		// TODO Auto-generated constructor stub
	}
	
	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}

	public String getCitlivost() {
		return citlivost;
	}

	public void setCitlivost(String citlivost) {
		this.citlivost = citlivost;
	}

	public String getPlatnyDo() {
		return platnyDo;
	}

	public void setPlatnyDo(String platnyDo) {
		this.platnyDo = platnyDo;
	}

	public String getPlatnyOd() {
		return platnyOd;
	}

	public void setPlatnyOd(String platnyOd) {
		this.platnyOd = platnyOd;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public String getPodpisanaDohoda() {
		return podpisanaDohoda;
	}

	public void setPodpisanaDohoda(String podpisanaDohoda) {
		this.podpisanaDohoda = podpisanaDohoda;
	}

	public String getTypSuhlasu() {
		return typSuhlasu;
	}

	public void setTypSuhlasu(String typSuhlasu) {
		this.typSuhlasu = typSuhlasu;
	}

}
