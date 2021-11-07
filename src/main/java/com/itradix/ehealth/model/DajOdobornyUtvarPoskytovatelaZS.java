package com.itradix.ehealth.model;

public class DajOdobornyUtvarPoskytovatelaZS {
	
	private String classification;
	private String date;
	private String oupzs;
	private String findOupzs;

	public String getOupzs() {
		return oupzs;
	}

	public void setOupzs(String oupzs) {
		this.oupzs = oupzs;
	}

	public String getFindOupzs() {
		return findOupzs;
	}

	public void setFindOupzs(String findOupzs) {
		this.findOupzs = findOupzs;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public DajOdobornyUtvarPoskytovatelaZS() {
		// TODO Auto-generated constructor stub
	}
	
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	
}
