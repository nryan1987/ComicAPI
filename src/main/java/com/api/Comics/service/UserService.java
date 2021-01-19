package com.api.Comics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.api.Comics.controllers.ComicController;
import com.api.Comics.models.User;
import com.api.Comics.repository.UserRepository;

@Service
public class UserService {
	
	Logger logger = LoggerFactory.getLogger(ComicController.class);
	
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
		user.setPassword(hashPassword(user.getPassword()));
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
	
	public String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	public boolean verifyHash(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

}
