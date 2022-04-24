package com.api.Comics.repository;

import org.springframework.data.repository.CrudRepository;

import com.api.Comics.entities.UserEntity;

import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends CrudRepository<UserEntity, String> {

	@Query(value = "SELECT UUID()", nativeQuery = true)
	String getUUID();
	
	@Query(value = "SELECT * FROM User u WHERE UserName=?1", nativeQuery = true)
	UserEntity findByUsername(String userName);
	
	@Query(value = "SELECT * FROM User u WHERE EmailAddress=?1", nativeQuery = true)
	UserEntity findByEmailAddress(String emailAddress);
}
