package com.api.Comics.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.Comics.models.User;

public interface UserRepository extends CrudRepository<User, String> {

	@Query(value = "SELECT UUID()", nativeQuery = true)
	String getUUID();
	
	@Query("SELECT u FROM User u WHERE UserName=?1")
	User findByUsername(String userName);
	
	@Query("SELECT u FROM User u WHERE EmailAddress=?1")
	User findByEmailAddress(String emailAddress);
}
