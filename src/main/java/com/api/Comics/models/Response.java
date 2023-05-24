package com.api.Comics.models;

import java.util.List;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Response {
	public Response() {}
	public Response(String message) {
		this.message = message;
	}
	
	private String message;
	private List<ResponseError> errors;
	private JSONObject body;
}
