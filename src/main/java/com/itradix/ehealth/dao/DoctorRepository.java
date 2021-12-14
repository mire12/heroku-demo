package com.itradix.ehealth.dao;

import org.springframework.stereotype.Repository;
import com.itradix.ehealth.model.ZdravotnyPracovnik;

@Repository
public interface DoctorRepository extends BaseRepository<ZdravotnyPracovnik, Long>{

}
