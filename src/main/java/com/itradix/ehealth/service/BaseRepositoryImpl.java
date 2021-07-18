package com.itradix.ehealth.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itradix.ehealth.dao.BaseRepository;
import com.itradix.ehealth.model.BaseEntity;
import com.itradix.ehealth.model.NcziResponse;
import com.itradix.ehealth.model.EzClassifications;

@Service
@Transactional
public class BaseRepositoryImpl<T extends BaseEntity, ID extends Serializable>
        implements BaseService<T, ID>{

    private BaseRepository<T, ID> abstractBaseRepository;

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
	public List<Object> findZdravotnickaOdbornostList(String oid, String oid_ver) {
		return abstractBaseRepository.findZdravotnickaOdbornostList(oid, oid_ver);
	}
	
	
	

}
