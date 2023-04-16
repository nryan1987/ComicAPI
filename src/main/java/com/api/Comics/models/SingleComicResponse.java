package com.api.Comics.models;

import lombok.Data;

@Data
public class SingleComicResponse extends Response {	
	public SingleComicResponse(String message) {
		super(message);
	}

	private ComicModel comicModel;
}
