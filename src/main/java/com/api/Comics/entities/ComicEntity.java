package com.api.Comics.entities;

import lombok.Data;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	@Column(name = "RecordCreationDate", /*insertable = false,*/ updatable = false)
	private Timestamp recordCreationDate;
	
	@Column(name = "LastUpdated", /*insertable = false,*/ updatable = false)
	private Timestamp lastUpdated;

	private LocalDate publicationDate;
}
