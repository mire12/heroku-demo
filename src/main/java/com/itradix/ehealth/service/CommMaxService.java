package com.itradix.ehealth.service;

import java.util.List;

import com.itradix.ehealth.model.EzClassifications;
import com.itradix.ehealth.model.NcziResponse;

public interface CommMaxService extends BaseService<NcziResponse, Long>{

	public String getCommmaxTemplate(String templateName);
	
	public List<NcziResponse> findNcziRespUsingNativeQuery(String evID);
	
	public List<NcziResponse> findNcziRespByDoctorAndPatient(String dID, String pID);
	
	public List<Object> findZdravotnickaOdbornostList(String oid, String oid_ver);
	
}
