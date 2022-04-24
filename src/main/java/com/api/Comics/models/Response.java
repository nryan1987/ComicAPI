package com.api.Comics.models;

import java.util.List;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Response {
	String message;
	List<ResponseError> errors;
	List<?> value;
	JSONObject body;
	
	public Response(String message) {
		this.message = message;
	}
	
}
