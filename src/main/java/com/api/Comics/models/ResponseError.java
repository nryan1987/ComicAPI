package com.api.Comics.models;

import lombok.Data;

@Data
public class ResponseError {
	String entity, errorMessage;

	public ResponseError(String entity, String errorMessage) {
		this.entity = entity;
		this.errorMessage = errorMessage;
	}
}
