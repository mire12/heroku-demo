package com.itradix.ehealth.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import com.itradix.ehealth.model.BaseEntity;
import com.itradix.ehealth.model.EzClassifications;
import com.itradix.ehealth.model.NcziResponse;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable>
extends JpaRepository<T, ID>{
	
	@Query(value = "select * from ncziresponse n where n.evid = ?1", nativeQuery = true)
	List<NcziResponse> findNcziRespUsingNativeQuery(String evID);
	
	@Query(value = "select c.oid,c.oid_ver,c.val,c.caption from ez_classifications c where c.oid = ?1 and c.oid_ver = ?2", nativeQuery = true)
	List<Object> findZdravotnickaOdbornostList(String oid, String oidVersion);

}
