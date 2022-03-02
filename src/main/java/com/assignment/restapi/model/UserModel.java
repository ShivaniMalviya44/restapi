package com.assignment.restapi.model;

import java.io.File;

public class UserModel {
	
	int id;
	String cwd;
	String username;
	String password;
	
	public UserModel() {
		
	}

	public UserModel(int id, String cwd, String username, String password) {
		super();
		this.id = id;
		this.cwd = cwd;
		this.username = username;
		this.password = password;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void display() {
		System.out.println("Id :"+getId()+"CWD :"+getCwd()+"UserName :"+getUsername()+"Password :"+getPassword());
	}
	
	public String curPath() {
		File file = new File("");                             //By File class
		String cwd = file.getAbsolutePath();
		return cwd;
		
		/*String cwd = System.getProperty("user.dir");           //BY System property
		return cwd;
		
		Path currentDirectoryPath = Paths.get("").toAbsolutePath();      //By Paths
		String cwd = currentDirectoryPath.toString();
		return cwd;
		
		String cwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();       //By FileSystems class
		return cwd;*/
	}

}
