package com.api.Comics.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.api.Comics.entities.ComicEntity;

public interface ComicRepository extends CrudRepository<ComicEntity, Integer> {
	
	@Query(value = "SELECT * FROM Comics c WHERE ComicID=?1", nativeQuery = true)
	ComicEntity findSingleIssue(Integer id);
	
	@Query(value = "SELECT * FROM Comics ORDER BY ComicID DESC LIMIT ?1", nativeQuery = true)
	List<ComicEntity> findLatestIssues(Integer numIssues);
	
	@Query(value = "SELECT DISTINCT c.title, c.publisher FROM Comics c", nativeQuery = true)
	List<Object[]> getTitlesAndPublishers();
	
	@Query(value = "SELECT Count(Comics.ComicID) AS CountOfComics, Sum(Comics.PricePaid) AS SumOfPricePaid,"
			+ "Sum(Comics.Value) AS SumOfValue, Avg(Comics.PricePaid) AS AveragePricePaid,"
			+ " Avg(Comics.Value) AS AverageValue FROM Comics", nativeQuery = true)
	Map<String, ?> getCollectionStats();
	
	@Query(value = "SELECT MAX(c.ComicID) from Comics c", nativeQuery = true)
	Integer getMaxComicID();
	
	@Query(value = "SELECT * FROM `Comics` "
			+ "ORDER BY Title, Volume, Issue", nativeQuery = true)
	Page<ComicEntity> getAllComics(Pageable page);
	
	@Query(value = "SELECT *"
			//+ " (SELECT GROUP_CONCAT(Notes SEPARATOR '; ') FROM Notes WHERE Notes.ComicID=Comics.ComicID ORDER BY Notes.Notes) AS Notes"
			+ " FROM `Comics`"
			+ "HAVING ComicID LIKE CONCAT('%', :searchTerm, '%')"
			+ " OR title LIKE CONCAT('%', :searchTerm, '%') OR issue LIKE CONCAT('%', :searchTerm, '%')"
			+ " OR StoryTitle LIKE CONCAT('%', :searchTerm, '%') OR publisher LIKE CONCAT('%', :searchTerm, '%')"
			//+ " OR Notes LIKE CONCAT('%', :searchTerm, '%')"
			+ "ORDER BY Title, Volume, Issue", nativeQuery = true)
	Page<ComicEntity> getAllComicsSearchTerm(@Param("searchTerm") String searchTerm, Pageable page);
}
