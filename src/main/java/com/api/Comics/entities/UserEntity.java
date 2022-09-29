package com.api.Comics.entities;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Column;
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
	
	@Column(name = "RecordCreationDate", insertable = false, updatable = false)
	private Timestamp recordCreationDate;
	@Column(name = "LastUpdatedDate", insertable = false, updatable = false)
	private Timestamp lastUpdatedDate;

}
