package com.assignment.restapi.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	/*HashMap<String, String>  userMap  = new HashMap<>(); 
	
	public HashMap<String, String> getHashMap() {
		return userMap;
	}

	public void setHashMap(HashMap<String, String> userMap) {
		this.userMap = userMap;
	}
	
	public void userDirectory(String username) {
		userMap.put(username, System.getProperty("user.dir"));
	}*/

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if(username.equals("Shivani")) {
			return new User("Shivani","shivi123", new ArrayList<>());			
		}
		else if(username.equals("Malviya")) {
			return new User("Malviya","malviya123", new ArrayList<>());			
		}
		else {
			LOGGER.error("User not found!!");
			throw new UsernameNotFoundException("User not found !!");
		}
	}
}
