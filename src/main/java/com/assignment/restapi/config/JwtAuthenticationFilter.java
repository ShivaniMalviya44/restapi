package com.assignment.restapi.config;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.assignment.restapi.services.CustomUserDetailsService;
import com.assignment.restapi.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//get jwt, check start with Bearer, Validate
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		//check format and null
		if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				LOGGER.info("Getting username from token");
				username = this.jwtUtil.getUsernameFromToken(jwtToken);
			}catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
			}catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            }catch(SignatureException e){
                logger.error("Authentication Failed. Username or Password not valid.");
            }
			
			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
			//security
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				LOGGER.info("Configuring spring security for user..");
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else {
				LOGGER.error("Token is not validated..");
				System.out.println("Token is not validated..");
			}
		}
		filterChain.doFilter(request, response);
	}

}

