package com.api.Comics.models;

import java.util.List;

import lombok.Data;

@Data
public class Response {
	String message;
	List<ResponseError> errors;
	
	public Response(String message) {
		this.message = message;
	}
	
}
