package com.itradix.ehealth.service;

import com.itradix.ehealth.model.NcziResponse;

public interface CommMaxService extends BaseService<NcziResponse, Long>{

	public String getCommmaxTemplate(String templateName);
	
}
