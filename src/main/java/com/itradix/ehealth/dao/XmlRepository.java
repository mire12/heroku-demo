package com.itradix.ehealth.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import com.itradix.ehealth.model.XmlTempObject;

@Repository
@Primary
public interface XmlRepository extends BaseRepository<XmlTempObject, Long>{

}
