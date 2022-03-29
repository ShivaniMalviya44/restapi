package com.assignment.restapi.model;

import org.springframework.stereotype.Component;

@Component
public class UserModel {
	
	private String cwd = System.getProperty("user.dir");
	private String username;

	public UserModel() {

	}

	public UserModel(String homedir, String cwd, String username) {
		super();
		this.username = username;
		this.cwd = cwd;
	}

	public String getCwd() {
		return cwd;
	}
	
	public void setCwd(String cwd) {
		this.cwd = cwd;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}
