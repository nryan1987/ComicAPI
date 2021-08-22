package com.api.Comics.entities;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "UserSettings")
public class UserSettingsEntity {
	
	@Id
	private String userID;
	private int numRecentIssues = 100;
	
	private Timestamp recordCreationDate, lastUpdatedDate;

}
