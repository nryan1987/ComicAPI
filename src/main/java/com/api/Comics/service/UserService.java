package com.api.Comics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.api.Comics.controllers.ComicController;
import com.api.Comics.entities.UserEntity;
import com.api.Comics.entities.UserSettingsEntity;
import com.api.Comics.repository.UserRepository;
import com.api.Comics.repository.UserSettingsRepository;

@Service
public class UserService {
	
	Logger logger = LoggerFactory.getLogger(ComicController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserSettingsRepository userSettingsRepository;
	
	public ResponseEntity<?> createUser(UserEntity user) {
		if(!isUsernameUnique(user.getUserName())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
		}
		
		if(!isEmailAddressUnique(user.getEmailAddress())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email address already exists");
		}

		user.setUserID(userRepository.getUUID());
		user.setPassword(hashPassword(user.getPassword()));
		userRepository.save(user);
		
		UserSettingsEntity settings = new UserSettingsEntity();
		settings.setUserID(user.getUserID());
		userSettingsRepository.save(settings);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created.");
	}
	
	public UserEntity getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public UserSettingsEntity getUserSettingsByUserID(String userID) {
		return userSettingsRepository.findByUserID(userID);
	}
	
	public boolean isUsernameUnique(String userName) {
		UserEntity u = userRepository.findByUsername(userName);
		
		return u == null;
	}
	
	public boolean isEmailAddressUnique(String emailAddress) {
		UserEntity u = userRepository.findByEmailAddress(emailAddress);
		
		return u == null;
	}
	
	public String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	public boolean verifyHash(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

}
