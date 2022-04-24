package com.api.Comics.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ComicUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		//Save hashed pwd in db.
		//Query db by uName and pwd.
		
		com.api.Comics.entities.UserEntity user = userService.getUserByUsername(username);
		
		return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
	}

}
