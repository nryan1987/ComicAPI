package com.api.Comics.repository;

import com.api.Comics.entities.ComicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

import static com.api.Comics.constants.ApplicationConstants.searchQuery;

public interface ComicRepository extends JpaRepository<ComicEntity, Integer> {

	@Query(value = "SELECT * FROM Comics ORDER BY ComicID DESC LIMIT ?1", nativeQuery = true)
	List<ComicEntity> findLatestIssues(Integer numIssues);

	List<ComicEntity> findByTitle(@Param(value = "title") String title);

	@Query(value = "SELECT DISTINCT c.title, MAX(c.Volume) as volume, c.publisher FROM Comics c GROUP BY c.Title, c.Publisher ", nativeQuery = true)
	List<Object[]> getTitlesAndPublishers();

	@Query(value = "SELECT DISTINCT c.title FROM Comics c ORDER BY c.Title", nativeQuery = true)
	List<String> getDistinctTitles();

	@Query(value = "SELECT Count(Comics.ComicID) AS CountOfComics, Sum(Comics.PricePaid) AS SumOfPricePaid,"
			+ "Sum(Comics.Value) AS SumOfValue, Avg(Comics.PricePaid) AS AveragePricePaid,"
			+ " Avg(Comics.Value) AS AverageValue FROM Comics", nativeQuery = true)
	Map<String, ?> getCollectionStats();

	@Query(value = "SELECT MAX(c.ComicID) from Comics c", nativeQuery = true)
	Integer getMaxComicID();

	@Query(value = searchQuery,
			countQuery = "SELECT count(*) FROM "
					+ " (" + searchQuery + ") as countQuery",
			nativeQuery = true)
	Page<ComicEntity> getAllComicsSearchTerm(@Param("searchTerm") String searchTerm, Pageable page);
}
