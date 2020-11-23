package com.itradix.ehealth.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.itradix.ehealth.dao.PatientRepository;
import com.itradix.ehealth.model.Patient;

@Service
@Transactional
public class PatientServiceImpl extends BaseRepositoryImpl<Patient, Long> 
        implements PatientService{
	
    public PatientServiceImpl(PatientRepository abstractBaseRepository) {
        super(abstractBaseRepository);
    }

}