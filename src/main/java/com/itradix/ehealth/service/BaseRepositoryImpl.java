package com.itradix.ehealth.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;

import com.itradix.ehealth.dao.BaseRepository;
import com.itradix.ehealth.model.BaseEntity;
import com.itradix.ehealth.model.NcziResponse;
import com.itradix.ehealth.model.EzClassifications;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
@Transactional
public class BaseRepositoryImpl<T extends BaseEntity, ID extends Serializable>
        implements BaseService<T, ID>{

    private BaseRepository<T, ID> abstractBaseRepository;
    
    
    @Autowired
    JpaTransactionManager transactionManager;

    @Autowired
    public BaseRepositoryImpl(BaseRepository<T, ID> abstractBaseRepository) {
        this.abstractBaseRepository = abstractBaseRepository;
    }

    @Override
    public T save(T entity) {
        return (T) abstractBaseRepository.save(entity);
    }

    @Override
    public List<T> findAll() {
        return abstractBaseRepository.findAll();
    }

    @Override
    public Optional<T> findById(ID entityId) {
        return abstractBaseRepository.findById(entityId);
    }
    

    @Override
    public T update(T entity) {
        return (T) abstractBaseRepository.save(entity);
    }

    @Override
    public T updateById(T entity, ID entityId) {
        Optional<T> optional = abstractBaseRepository.findById(entityId);
        if(optional.isPresent()){
            return (T) abstractBaseRepository.save(entity);
        }else{
            return null;
        }
    }

    @Override
    public void delete(T entity) {
        abstractBaseRepository.delete(entity);
    }

    @Override
    public void deleteById(ID entityId) {
        abstractBaseRepository.deleteById(entityId);
    }
	
	@Transactional
	public List<NcziResponse> findNcziRespUsingNativeQuery(String evID) {
		List<NcziResponse> studentResponse = abstractBaseRepository.findNcziRespUsingNativeQuery(evID);
		return studentResponse;
	}
	
	@Transactional
	public List<NcziResponse> findNcziRespByDoctorAndPatient(String dID, String pID) {
		List<NcziResponse> studentResponse = abstractBaseRepository.findNcziRespByDoctorAndPatient(dID, pID);
		return studentResponse;
	}
	
	@Transactional
	public List<NcziResponse> searchNcziResp(String dID, String pID, LocalDateTime dateTime) {
		EntityManagerFactory emf =  transactionManager.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<NcziResponse> cr = cb.createQuery(NcziResponse.class);
		Root<NcziResponse> root = cr.from(NcziResponse.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(!dID.equals(""))
			predicates.add(cb.like(root.get("doctorID"), dID));
		if(!pID.equals(""))
			predicates.add(cb.like(root.get("patientID"), pID));
		predicates.add(cb.greaterThanOrEqualTo(root.<LocalDateTime>get("createdAt"), dateTime.with(LocalTime.MIN)));
		predicates.add(cb.lessThanOrEqualTo(root.<LocalDateTime>get("createdAt"), dateTime.with(LocalTime.MAX)));
		
		cr.select(root).where(predicates.toArray(Predicate[]::new));
		
	    return em.createQuery(cr).getResultList();
		
		
		
		//List<NcziResponse> ncziResponses = abstractBaseRepository.searchNcziResp(dID, pID, Timestamp.valueOf(dateTime.with(LocalTime.MIN)), Timestamp.valueOf(dateTime.with(LocalTime.MAX)));
		//return ncziResponses;
	}
	
	@Transactional
	public List<Object> findZdravotnickaOdbornostList(String oid, String oid_ver) {
		return abstractBaseRepository.findZdravotnickaOdbornostList(oid, oid_ver);
	}
	
	@Transactional
	public List<Object> findOdbornyUtvarPoskytovatelaZS(String idoupzs) {
		return abstractBaseRepository.findOdbornyUtvarPoskytovatelaZS(idoupzs);
	}
	
	@Transactional
	public List<Object> findOdbornyUtvarPoskytovatelaZSList(String obec) {
		return abstractBaseRepository.findOdbornyUtvarPoskytovatelaZSList(obec);
	}
	
	@Transactional
	public List<Object> findCiselnikIdentifikatorList(String oid, String oidver) {
		return abstractBaseRepository.findCiselnikIdentifikator(oid, oidver);
	}
	
	
	
	
	

}
