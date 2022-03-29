package com.assignment.restapi.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(MyController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserModel userModel;

	HashMap<String, String> hashmap = new HashMap<>();

	public HashMap<String, String> getHashmap() {
		return hashmap;
	}

	public void setHashmap(HashMap<String, String> hashmap) {
		this.hashmap = hashmap;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		// System.out.println(jwtRequest);
		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

		} catch (UsernameNotFoundException e) {
			LOGGER.error("User not found!");
			e.printStackTrace();
			throw new Exception("Usename not found");
		} catch (BadCredentialsException e) {
			LOGGER.error("Bad Credential!");
			e.printStackTrace();
			throw new Exception("Bad Credentials");
		}

		// fine area..
		LOGGER.trace("Entering Method loadUserByUsername");
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

		// to generate token
		String Token = this.jwtUtil.generateToken(userDetails);
		// customUserDetailsService.userDirectory(userDetails.getUsername());
		LOGGER.info("Generated Token");
		System.out.println("JWT " + Token);

		// return token in json
		// {"token":"value"}
		return ResponseEntity.ok(new JwtResponse(Token));
	}

	/*
	 * // One more way to get current logged in Username
	 * 
	 * @RequestMapping(path = "/cwd") 
	 * public String getCurrentWorkingDirectory(HttpServletRequest request){
	 * UserDetails userDetails = (UserDetails).SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
	 * String username = userDetails.getUsername(); return username; 
	 * }
	 */
	
	@RequestMapping(path = "/cwd")
	public ResponseEntity<?> getCurrentWorkingDirectory(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		String path = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			username = this.jwtUtil.getUsernameFromToken(jwtToken);
		}
		
        if(!hashmap.containsKey(username)) {                           //if(!true)
        	hashmap.put(username, userModel.getCwd());
        }
     
		try {
			path = hashmap.get(username);	
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("Returning Current Working Directory..");
		return new ResponseEntity<>(path, HttpStatus.OK);
	}

	/*
	 * @GetMapping(path = "/cwd", produces = MediaType.APPLICATION_JSON_VALUE)
	 * public String cwd() { 
	 * HashMap<String, String> userMap = customUserDetailsService.getHashMap(); 
	 * String username = SecurityContextHolder.getContext().getAuthentication().getName(); 
	 * String route = userMap.get(username);
	 * LOGGER.info("Returning current working directory"); 
	 * return ("CWD :" + route);
	 * }
	 */

	@GetMapping(path = "/ls", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<LsModel> ls(HttpServletRequest request) {
		ArrayList<LsModel> arraylist = new ArrayList<LsModel>();
		LOGGER.info("Entering in the /ls method..");
		String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			username = this.jwtUtil.getUsernameFromToken(jwtToken);
		}
		if(!hashmap.containsKey(username)) {
	        	hashmap.put(username, userModel.getCwd());
	    }
		String cwd = hashmap.get(username);
		//String cwd = userModel.getCwd();
		File curDir = new File(cwd);
		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			if (f.isFile()) {
				LsModel obj = new LsModel(f.getName(), "FILE");
				arraylist.add(obj);
			} else if (f.isDirectory()) {
				LsModel obj = new LsModel(f.getName(), "DIRECTORY");
				arraylist.add(obj);
			}
		}
		LOGGER.info("Returning list of files and directories in the current working directory..");
		return (arraylist);
	}

	@GetMapping(path = "/cd/{dir}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String cd(@PathVariable(name = "dir") String dir) throws FileNotFoundException {
		LOGGER.info("Entering in the change directory method..");
		String username = SecurityContextHolder.getContext().getAuthentication().getName(); 
		if(!hashmap.containsKey(username)) {
	        	hashmap.put(username, userModel.getCwd());
	    }
		String rootDir = hashmap.get(username);
		String new_Path = null;
		//String rootDir = userModel.getCwd();
		Path filePath = Paths.get(dir);
		if (Files.exists(filePath)) {
			hashmap.replace(username, rootDir + "/" + dir);
			new_Path = hashmap.get(username);
			//userModel.setCwd(rootDir + "/" + dir);
		} else {
			LOGGER.error("Directory not found!!!!");
			throw new FileNotFoundException("Not found!!!!");
		}
		LOGGER.info("Returning updated directory..");
		return (new_Path);
	}
}
