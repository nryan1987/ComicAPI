package com.api.Comics.models;

import lombok.Data;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Comics")
public class Comic {
	
	@Id
	private int ComicID;
	
	private int volume,
	issue;
	
	private double pricePaid, value;

	private String title, storyTitle, publisher, condition, picture;

	private Timestamp recordCreationDate,
	lastUpdated;

	private LocalDate publicationDate;
	
	public boolean doesPictureExist() {
		if(this.picture == null || this.picture.length() == 0)
			return false;
		
		File picFile = new File(this.picture);
		return picFile.exists();
	}
}
