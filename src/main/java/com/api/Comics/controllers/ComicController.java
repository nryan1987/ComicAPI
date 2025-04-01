package com.api.Comics.controllers;

import com.api.Comics.entities.ComicEntity;
import com.api.Comics.models.*;
import com.api.Comics.models.response.TitlesAndPublishersResponse;
import com.api.Comics.repository.ComicRepository;
import com.api.Comics.service.ComicService;
import com.api.Comics.service.PublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.api.Comics.constants.APIConstants.*;

@RestController
@CrossOrigin
@RequestMapping(COMIC)
public class ComicController {
	@Autowired
	private ComicRepository comicRepository;

	@Autowired
	private ComicService comicService;

	Logger logger = LoggerFactory.getLogger(ComicController.class);

	@GetMapping("/{id}")
	public ResponseEntity<SingleComicResponse> oneComic(@PathVariable int id) {
		logger.info("Comic controller " + id);
		return comicService.getComic(id);
	}
	
	@GetMapping("/collectionStats")
	public CollectionStats getCollectionStats() {
		logger.info("collectionStats");
		
		return comicService.getCollectionStats();
	}

	@PostMapping("/latestIssues")
	public List<ComicEntity> getLatestIssues(@RequestBody int numIssues) {
		logger.info("latest100Issues " + numIssues);
		return comicService.latestIssues(numIssues);
	}
	
	@PostMapping(FIND_BY_TITLE)
	public ResponseEntity<FindByTitleResponse> findByTitle(@RequestBody FindByTitleRequest findByTitleRequest) {
		return comicService.findByTitle(findByTitleRequest.getTitle());
	}

	@GetMapping("/all")
	public List<ComicEntity> allComics() {
		logger.info("All comics endpoint");

		return (List<ComicEntity>) comicRepository.findAll();
	}

	@GetMapping(TITLES)
	public ResponseEntity<TitlesAndPublishersResponse> getTitlesAndPublisherMap() {
		logger.info("Titles and publishers endpoint");
		TitlesAndPublishersResponse response = comicService.getTitlesAndPublishers();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/distinctTitles")
	public List<String> uniqueTitles() {
		logger.info("Titles endpoint");
		return comicService.getDistinctTitles();
	}
	
	@PostMapping("/addComics")
	public ResponseEntity<?> addComics(@RequestBody List<ComicModel> comics) {
		logger.info("addComics " + comics);
		
		List<ResponseError> errors = comicService.addComicsList(comics);
		
		if(errors.size() > 0) {
			Response r = new Response("Some comics failed to save");
			r.setErrors(errors);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
		}
		
		return ResponseEntity.ok(new Response("All comics added successfully."));
	}
	
	@PostMapping("/updateComic")
	public ResponseEntity<SingleComicResponse> updateComic(@RequestBody UpdateComicRequest updateComicRequest) {
		return comicService.updateComic(updateComicRequest);
	}
	
	@PostMapping(GET_PAGE)
	public ResponseEntity<ComicPageResponse> getComicsPage(@RequestBody GetPageRequest getPageRequest) {
		return comicService.getComicsPage(getPageRequest.getPageNumber(), getPageRequest.getPageSize(), getPageRequest.getSearchTerm());
	}
}
