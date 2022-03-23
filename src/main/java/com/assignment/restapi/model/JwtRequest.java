package com.assignment.restapi.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtRequest  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequest .class);
	
	String username;
	String password;
	
	public JwtRequest() {
	
	}

	public JwtRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		LOGGER.info("Returning Usename and Password");
		return "JwtRequest [username=" + username + ", password=" + password + "]";
	}
}
