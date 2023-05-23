package com.api.Comics.models;

import java.time.LocalDate;
import java.util.List;

import com.api.Comics.entities.NoteEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class SingleComicResponse extends Response {	
	public SingleComicResponse() {}
	public SingleComicResponse(String message) {
		super(message);
	}
	
	private int ComicID, volume;
	
	private double issue, pricePaid, value;
	
	private String title, storyTitle, publisher, picture, condition;

	private String recordCreationDate, lastUpdated;

	private LocalDate publicationDate;
	
	List<NoteEntity> notes; 
}
