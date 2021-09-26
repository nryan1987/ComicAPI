package com.api.Comics.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.api.Comics.entities.PublisherEntity;

public interface PublisherRepository extends CrudRepository<PublisherEntity, Integer> {
	@Query(value = "SELECT MAX(p.PublisherID) from Publisher p", nativeQuery = true)
	Integer getMaxPublisherID();
}
