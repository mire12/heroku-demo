package com.itradix.ehealth.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.itradix.ehealth.model.BaseEntity;
import com.itradix.ehealth.model.NcziResponse;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable>
extends JpaRepository<T, ID>{
	
	@Query(value = "select * from ncziresponse n where n.did = ?1 and n.pid = ?2", nativeQuery = true)
	List<NcziResponse> findNcziRespByDoctorAndPatient(String dID, String pID);
	
	
	@Query(value = "select * from ncziresponse n " +
			 "where (:dID is null or n.did like :dID) " +
	            " and " +
	            " (:pID is null or n.pid like :pID)" +
	            " and " +
	            " ((cast(n.createdat as timestamp)) >= :startDate)" +
	            " and " +
	            " ((cast(n.createdat as timestamp)) < :endDate)", nativeQuery = true)
	List<NcziResponse> searchNcziResp(@Param("dID") String dID, @Param("pID") String pID, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
	
	
	/*CriteriaQuery<NcziResponse> criteriaQuery = 
			  criteriaBuilder.createQuery(NcziResponse.class);
			Root<DeptEmployee> root = criteriaQuery.from(NcziResponse.class);
			In<String> inClause = criteriaBuilder.in(root.get("title"));
			for (String title : titles) {
			    inClause.value(title);
			}
			criteriaQuery.select(root).where(inClause);*/
	
	@Query(value = "select * from ncziresponse n where n.evid = ?1", nativeQuery = true)
	List<NcziResponse> findNcziRespUsingNativeQuery(String evID);
	
	@Query(value = "select c.oid,c.oid_ver,c.val,c.caption from ez_classifications c where c.oid = ?1 and c.oid_ver = ?2", nativeQuery = true)
	List<Object> findZdravotnickaOdbornostList(String oid, String oidVersion);

}
