package com.itradix.ehealth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itradix.ehealth.dao.UserRepository;
import com.itradix.ehealth.model.EhealthUser;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
	private PasswordEncoder bcryptEncoder;
    
    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
    	
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username)));
    }
    
    public EhealthUser save(EhealthUser ehealthUser) {
		ehealthUser.setPassword(bcryptEncoder.encode(ehealthUser.getPassword()));
		return userRepository.save(ehealthUser);
	}

}