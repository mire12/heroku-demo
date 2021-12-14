package com.itradix.ehealth.model;

import sk.gov.ehealth.common.v1.UserContext;

public class ZrusSumarUdaje {

	private UserContext userContext;
	private String rcIdExtensionDeleted;
	private String rcIdOid;
	
	
	public ZrusSumarUdaje() {
		// TODO Auto-generated constructor stub
	}


	public UserContext getUserContext() {
		return userContext;
	}


	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}


	public String getRcIdExtensionDeleted() {
		return rcIdExtensionDeleted;
	}


	public void setRcIdExtensionDeleted(String rcIdExtensionDeleted) {
		this.rcIdExtensionDeleted = rcIdExtensionDeleted;
	}


	public String getRcIdOid() {
		return rcIdOid;
	}


	public void setRcIdOid(String rcIdOid) {
		this.rcIdOid = rcIdOid;
	}


}
