package com.api.Comics.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.Comics.models.AuthenticationRequest;
import com.api.Comics.models.AuthenticationResponse;
import com.api.Comics.models.User;
import com.api.Comics.service.ComicUserDetailsService;
import com.api.Comics.service.UserService;
import com.api.Comics.util.JwtUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private ComicUserDetailsService udService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private Logger logger = LoggerFactory.getLogger(ComicController.class);
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) {
		try {
			authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
			);
		} catch(BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthenticationResponse(null, "Incorrect username or password"));
		}
		
		final UserDetails userDetails = udService.loadUserByUsername(authRequest.getUsername());
		final String token = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(token, "Token created successfully"));
	}
	
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		logger.info("createUser endpoint");		
		return userService.createUser(user);
	}
}
