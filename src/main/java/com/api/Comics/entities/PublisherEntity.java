package com.api.Comics.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Publisher")
public class PublisherEntity {
	@Id
	private int PublisherID;
	
	@Column(name = "Publisher")
	private String Publisher;
	
	//The value for RecordCreationDate are handled on the DB side.
	@Column(name = "RecordCreationDate", insertable = false, updatable = false)
	private Timestamp recordCreationDate;
}
