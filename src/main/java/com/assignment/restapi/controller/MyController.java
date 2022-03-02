package com.assignment.restapi.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.restapi.model.JwtRequest;
import com.assignment.restapi.model.JwtResponse;
import com.assignment.restapi.model.LsModel;
import com.assignment.restapi.model.UserModel;
import com.assignment.restapi.services.CustomUserDetailsService;
import com.assignment.restapi.util.JwtUtil;


@RestController
public class MyController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private UserModel userModel= new UserModel() ;
    private LsModel lsModel= new LsModel() ;
	
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		//System.out.println(jwtRequest);
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
			
		}catch(UsernameNotFoundException e)
		{
			e.printStackTrace();
			throw new Exception("Bad Credential");
		}catch(BadCredentialsException e)
		{
			e.printStackTrace();
			throw new Exception("Bad Credential");
		}
		
		//fine area..
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
		
		//to generate token
		String Token = this.jwtUtil.generateToken(userDetails);
		System.out.println("JWT "+Token);
		
		//return token in json 
		//{"token":"value"}
		return ResponseEntity.ok(new JwtResponse(Token));
	}

	@GetMapping(path = "/cwd", produces = MediaType.APPLICATION_JSON_VALUE)
	public String cwd() {
		//String cwd = userModel.curPath();
		 String cwd = userModel.getCwd();
	     return cwd;
	}	
	
	@GetMapping(path = "/ls", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<LsModel> ls() {
		ArrayList<LsModel> arraylist = lsModel.list();
		return (arraylist);
	}
	
	@GetMapping(path = "/cd/{dir}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String cd(@PathVariable(name= "dir") String dir) {
		//String newPath = "";
		String rootDir = userModel.curPath();	
		Path filePath = Paths.get(dir);
		if(Files.exists(filePath)) {
			userModel.setCwd(rootDir + "/"+  dir);	
		}
		 else { 
			    return "Not found!!";
			 }
		return (rootDir + "/"+  dir);		
	}
}
