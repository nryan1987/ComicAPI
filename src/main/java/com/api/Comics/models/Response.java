package com.api.Comics.models;

import lombok.Data;

@Data
public class Response {
	String message;
	
	public Response(String message) {
		this.message = message;
	}
	
}
