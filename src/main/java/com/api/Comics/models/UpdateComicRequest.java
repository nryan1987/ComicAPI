package com.api.Comics.models;

import java.time.LocalDate;
import java.util.List;

import com.api.Comics.entities.NoteEntity;

import lombok.Data;

@Data
public class UpdateComicRequest {
	private int ComicID, volume;
	
	private double issue, pricePaid, value;
	
	private String title, storyTitle, publisher, picture, condition;

	private LocalDate publicationDate;
	
	List<NoteEntity> notes;
	List<NoteEntity> deletedNotes;
}
