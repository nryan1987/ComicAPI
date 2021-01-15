package com.api.Comics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.api.Comics.models.Comic;

public interface ComicRepository extends CrudRepository<Comic, Integer> {
	
	@Query("SELECT c FROM Comic c WHERE ComicID=?1")
	Comic findSingleIssue(Integer id);
	
	@Query("SELECT DISTINCT c.title FROM Comic c")
	List<String> findUniqueTitles();
}
