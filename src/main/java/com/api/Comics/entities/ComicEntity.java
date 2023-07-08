package com.api.Comics.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(value = { "notes" })
@Data
@Entity
@Table(name = "Comics")
public class ComicEntity {
	
	@Id
	private int ComicID;
	
	private int volume;
	
	private double issue, pricePaid, value;

	@Column(name="`condition`")
	private String condition;
	
	private String title, storyTitle, publisher, picture;

	//The values for RecordCreationDate and LastUpdated are handled on the DB side.
	@Column(name = "RecordCreationDate", insertable = false, updatable = false)
	private Timestamp recordCreationDate;
	
	@Column(name = "LastUpdated", insertable = false, updatable = false)
	private Timestamp lastUpdated;

	private LocalDate publicationDate;

	@OneToMany(mappedBy = "comicEntity")
	@OrderBy("Notes ASC")
	private List<NoteEntity> notes;
}
