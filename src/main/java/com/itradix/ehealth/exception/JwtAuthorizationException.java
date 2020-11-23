package com.itradix.ehealth.exception;

import javax.servlet.ServletException;

public class JwtAuthorizationException extends ServletException {
	
		private final String message;

	    public static JwtAuthorizationException createWith(String message) {
	        return new JwtAuthorizationException(message);
	    }

	    private JwtAuthorizationException(String message) {
	        this.message = message;
	    }

	    @Override
	    public String getMessage() {
	        return this.message;
	    }
	}