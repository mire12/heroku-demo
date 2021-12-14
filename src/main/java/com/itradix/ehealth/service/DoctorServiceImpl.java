package com.itradix.ehealth.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.itradix.ehealth.dao.DoctorRepository;
import com.itradix.ehealth.model.ZdravotnyPracovnik;

@Service
@Transactional
public class DoctorServiceImpl extends BaseRepositoryImpl<ZdravotnyPracovnik, Long> 
        implements DoctorService{
	
    public DoctorServiceImpl(DoctorRepository abstractBaseRepository) {
        super(abstractBaseRepository);
    }

}