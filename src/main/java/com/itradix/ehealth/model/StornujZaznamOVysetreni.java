package com.itradix.ehealth.model;

public class StornujZaznamOVysetreni {

	private String classification;

	private String datetime;
	
	private String rcIdDeleted;
	
	private String deleteVersion;
	
	private String deletionText;
	
	private String oupzs;
	

	public StornujZaznamOVysetreni() {
		// TODO Auto-generated constructor stub	
			
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	public String getRcIdDeleted() {
		return rcIdDeleted;
	}

	public void setRcIdDeleted(String rcIdDeleted) {
		this.rcIdDeleted = rcIdDeleted;
	}
	
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public String getDeleteVersion() {
		return deleteVersion;
	}


	public void setDeleteVersion(String deleteVersion) {
		this.deleteVersion = deleteVersion;
	}


	public String getDeletionText() {
		return deletionText;
	}


	public void setDeletionText(String deletionText) {
		this.deletionText = deletionText;
	}

	public String getOupzs() {
		return oupzs;
	}

	public void setOupzs(String oupzs) {
		this.oupzs = oupzs;
	}

	
	
}
