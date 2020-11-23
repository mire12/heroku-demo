package com.itradix.ehealth.exception;

public class UserNotFoundException extends Exception {
	
		private final String username;

	    public static UserNotFoundException createWith(String username) {
	        return new UserNotFoundException(username);
	    }

	    private UserNotFoundException(String username) {
	        this.username = username;
	    }

	    @Override
	    public String getMessage() {
	        return "BAD CREDENTIALS for user : '" + username + "'";
	    }
	}