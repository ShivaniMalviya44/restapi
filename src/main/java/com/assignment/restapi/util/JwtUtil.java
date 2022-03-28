package com.assignment.restapi.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//methods - for generating token, validate, isExpire

@Component
public class JwtUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
	
	public static final long JWT_TOKEN_VALIDITY = 3 * 60 ;
	
	//@Value("${jwt.secret}")
	private String secret="assignmentapi";
	
	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		LOGGER.info("retrieve username from jwt token");
		return getClaimFromToken(token, Claims::getSubject);
	}
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		LOGGER.info("retrieve expiration date from jwt token");
		return getClaimFromToken(token, Claims::getExpiration);
	}
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    //for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	/*//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}*/
	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string 
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		LOGGER.info("Token Generation..");
		LOGGER.debug("while creating the token claims of the token, like Issuer, Expiration, Subject, and the ID");
		LOGGER.info("while creating the token claims of the token, like Issuer, Expiration, Subject, and the ID");
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	
	/*//validate token 
	  public Boolean validateToken(String token, UserDetails userDetails) { LOGGER.info("Token Validation.."); 
	    final String username = getUsernameFromToken(token); 
	    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
	  }*/	 
}