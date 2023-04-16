package com.api.Comics.models;

import java.util.List;

import lombok.Data;

@Data
public class PublisherListResponse extends Response {
	public PublisherListResponse(String message) {
		super(message);
	}
	List<String> publishers;
}
