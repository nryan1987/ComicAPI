package com.api.Comics.repository;

import org.springframework.data.repository.CrudRepository;

import com.api.Comics.entities.UserSettingsEntity;

import org.springframework.data.jpa.repository.Query;

public interface UserSettingsRepository extends CrudRepository<UserSettingsEntity, String> {	
	@Query(value = "SELECT * FROM UserSettings u WHERE UserID=?1", nativeQuery = true)
	UserSettingsEntity findByUserID(String userID);
}
