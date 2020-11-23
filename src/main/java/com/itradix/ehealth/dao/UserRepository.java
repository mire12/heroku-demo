package com.itradix.ehealth.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itradix.ehealth.model.EhealthUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<EhealthUser, Long> {
    Optional<EhealthUser> findByUsername(String username);
}