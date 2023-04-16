package com.api.Comics.models;

import java.util.List;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Response {
	private String message;
	private List<ResponseError> errors;
	private List<?> value;
	private JSONObject body;
	
	public Response(String message) {
		this.message = message;
	}
	
}
