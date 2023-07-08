package com.api.Comics.controllers;

import com.api.Comics.entities.ComicEntity;
import com.api.Comics.models.*;
import com.api.Comics.repository.ComicRepository;
import com.api.Comics.service.ComicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.api.Comics.constants.APIConstants.COMIC;
import static com.api.Comics.constants.APIConstants.GET_PAGE;

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
	
	@PostMapping("/findByTitle")
	//public List<ComicEntity> findByTitle(@RequestBody Map<Object, Object> parameters) {
	public List<ComicEntity> findByTitle(@RequestBody String parameters) {
		logger.info("Find by title: {}", parameters);
		//String title = (String) parameters.get("title");
		List<ComicEntity> comics = comicService.findByTitle(parameters); 
		return comics;
	}

	@GetMapping("/all")
	public List<ComicEntity> allComics() {
		logger.info("All comics endpoint");

		return (List<ComicEntity>) comicRepository.findAll();
	}

	@GetMapping("/titles")
	public List<ComicEntity> getTitlesAndPublisherMap() {
		logger.info("Titles and publishers endpoint");

		return comicService.getTitlesAndPublishers();
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
