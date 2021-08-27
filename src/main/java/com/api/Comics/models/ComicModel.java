package com.api.Comics.models;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import com.api.Comics.entities.ComicEntity;
import com.api.Comics.entities.NoteEntity;

import lombok.Data;

@Data
public class ComicModel {
	private int ComicID, volume;
	
	private double issue, pricePaid, value;
	
	private String title, storyTitle, publisher, picture, condition;

	private Timestamp recordCreationDate, lastUpdated;

	private LocalDate publicationDate;
	
	List<NoteEntity> notes; 
		
	public boolean doesPictureExist() {
		if(this.picture == null || this.picture.length() == 0)
			return false;
		
		File picFile = new File(this.picture);
		return picFile.exists();
	}
	
	public ComicEntity getComicEntity() {
		ComicEntity ce = new ComicEntity();
		ce.setComicID(this.ComicID);
		ce.setIssue(this.issue);
		ce.setVolume(this.volume);
		ce.setTitle(this.title);
		ce.setStoryTitle(this.storyTitle);
		ce.setPublisher(this.publisher);
		ce.setPicture(this.picture);
		ce.setCondition(this.condition);
		ce.setRecordCreationDate(this.recordCreationDate);
		ce.setLastUpdated(this.lastUpdated);
		ce.setPublicationDate(this.publicationDate);
		ce.setPricePaid(this.pricePaid);
		
		return ce;
	}
}
