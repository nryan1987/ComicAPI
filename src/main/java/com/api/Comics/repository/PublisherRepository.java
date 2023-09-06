package com.api.Comics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.Comics.entities.PublisherEntity;

public interface PublisherRepository extends JpaRepository<PublisherEntity, Integer> {
	@Query(value = "SELECT MAX(p.PublisherID) from Publisher p", nativeQuery = true)
	Integer getMaxPublisherID();
}
