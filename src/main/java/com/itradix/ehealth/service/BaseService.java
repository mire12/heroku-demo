package com.itradix.ehealth.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.data.jpa.repository.Query;

import com.itradix.ehealth.model.BaseEntity;
import com.itradix.ehealth.model.NcziResponse;

public interface BaseService<T extends BaseEntity, ID extends Serializable>{
    public abstract T save(T entity);
    public abstract List<T> findAll(); 

    public abstract Optional<T> findById(ID entityId);
    
    public abstract T update(T entity);
    public abstract T updateById(T entity, ID entityId);   
    public abstract void delete(T entity);
    public abstract void deleteById(ID entityId);

}
