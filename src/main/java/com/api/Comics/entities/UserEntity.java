package com.api.Comics.entities;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "User")
public class UserEntity {
	
	@Id
	private String userID;
	private String userName, password, emailAddress;
	
	private Timestamp recordCreationDate, lastUpdatedDate;

}
