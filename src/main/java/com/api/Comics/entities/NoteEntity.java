package com.api.Comics.entities;

import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties(value = { "comicEntity" })
@Data
@Entity
@Table(name = "Notes")
public class NoteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NoteID", insertable = false, updatable = false)
	private int NoteID;
	
	@Column(name = "ComicID")
	private int ComicID;
	
	@Column(name="Notes")
	private String Notes;
	
	//The values for RecordCreationDate and LastUpdated are handled on the DB side.
	@Column(name = "RecordCreationDate", insertable = false, updatable = false)
	private Timestamp recordCreationDate;

	@Column(name = "LastUpdated", insertable = false, updatable = false)
	private Timestamp lastUpdated;

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "ComicID", nullable = false, insertable = false, updatable = false)
	private ComicEntity comicEntity;
}
