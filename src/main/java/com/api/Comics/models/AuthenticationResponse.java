package com.api.Comics.models;

import com.api.Comics.entities.UserSettingsEntity;

import lombok.Data;

@Data
public class AuthenticationResponse {
	private final String jwt;
	private final String message;
	private final UserSettingsEntity userSettings;
	
	public AuthenticationResponse(String jwt, UserSettingsEntity userSettings, String message) {
		this.jwt = jwt;
		this.userSettings = userSettings;
		this.message = message;
	}
	
}
