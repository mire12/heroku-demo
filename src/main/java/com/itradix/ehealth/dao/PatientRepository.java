package com.itradix.ehealth.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import com.itradix.ehealth.model.Patient;

@Repository
@Primary
public interface PatientRepository extends BaseRepository<Patient, Long>{

}
