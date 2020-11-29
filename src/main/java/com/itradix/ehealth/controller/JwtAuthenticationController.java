package com.itradix.ehealth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itradix.ehealth.JwtTokenUtil;
import com.itradix.ehealth.exception.UserNotFoundException;
import com.itradix.ehealth.model.EhealthUser;
import com.itradix.ehealth.model.JwtRequest;
import com.itradix.ehealth.model.JwtResponse;
import com.itradix.ehealth.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@CrossOrigin(origins = "https://ehealth-ng-app.herokuapp.com")
	@PostMapping(value = "/authenticate")
	public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
		throws UserNotFoundException {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));

	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/register")
	public ResponseEntity<EhealthUser> saveUser(@RequestBody EhealthUser user) {
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	private void authenticate(String username, String password) throws UserNotFoundException {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (BadCredentialsException e) {
			throw UserNotFoundException.createWith(username);
		}

	}
	
	@CrossOrigin(origins = "https://ehealth-ng-app.herokuapp.com")
	@GetMapping(value = "/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("UP");
	}
	
	@CrossOrigin(origins = "https://ehealth-ng-app.herokuapp.com")
	@GetMapping(value = "/error")
	public ResponseEntity<String> error() {
		return ResponseEntity.ok("ERROR");
	}

}
