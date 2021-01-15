package com.api.Comics.models;

import lombok.Data;

@Data
public class AuthenticationRequest {
	private String username, password;

	public AuthenticationRequest() {}
	
	public AuthenticationRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	
}
