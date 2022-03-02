package com.assignment.restapi.model;

import java.io.File;
import java.util.ArrayList;

public class LsModel {
	
	private UserModel userModel = new UserModel() ;
	
	private String name;
	private String type;
	
	public LsModel() {
	
	}

	public LsModel(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<LsModel> list(){
	ArrayList<LsModel> arraylist = new ArrayList<LsModel>();
    //File curDir = new File(".");
    String cwd = userModel.curPath();
    File curDir = new File(cwd);
    File[] filesList = curDir.listFiles();
    for(File f : filesList) {
        if(f.isFile()) {
        	LsModel obj = new LsModel(f.getName(), "FILE");
        	arraylist.add(obj);
        }
        else if (f.isDirectory()){
        	LsModel obj = new LsModel(f.getName(), "DIRECTORY");
        	arraylist.add(obj);
        }  
    }
    return (arraylist);
	}
}

