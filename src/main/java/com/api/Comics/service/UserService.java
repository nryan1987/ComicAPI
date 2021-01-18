package com.api.Comics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.Comics.models.User;
import com.api.Comics.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public ResponseEntity<?> createUser(User user) {
		
		if(!isUsernameUnique(user.getUserName())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
		}
		
		if(!isEmailAddressUnique(user.getEmailAddress())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email address already exists");
		}
		
		user.setUserID(userRepository.getUUID());
		userRepository.save(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created.");
	}
	
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public boolean isUsernameUnique(String userName) {
		User u = userRepository.findByUsername(userName);
		
		return u == null;
	}
	
	public boolean isEmailAddressUnique(String emailAddress) {
		User u = userRepository.findByEmailAddress(emailAddress);
		
		return u == null;
	}

}
