package com.api.Comics.models;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "User")
public class User {
	
	@Id
	private String userID;
	private String userName, password, emailAddress;
	
	private Timestamp recordCreationDate, lastUpdatedDate;

}
