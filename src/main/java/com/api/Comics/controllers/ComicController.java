package com.api.Comics.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.Comics.models.Response;
import com.api.Comics.entities.ComicEntity;
import com.api.Comics.models.CollectionStats;
import com.api.Comics.models.ComicModel;
import com.api.Comics.repository.ComicRepository;
import com.api.Comics.service.ComicService;

@RestController
@CrossOrigin
@RequestMapping("/comic")
public class ComicController {
	@Autowired
	private ComicRepository comicRepository;

	@Autowired
	private ComicService comicService;

	Logger logger = LoggerFactory.getLogger(ComicController.class);

	@GetMapping("/{id}")
	public ComicEntity oneComic(@PathVariable int id) {
		logger.info("Comic controller " + id);

		Optional<ComicEntity> c = comicRepository.findById(id);
		logger.info(c.get().toString());

		return c.get();
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

	@GetMapping("/all")
	public List<ComicEntity> allComics() {
		logger.info("All comics endpoint");

		return (List<ComicEntity>) comicRepository.findAll();
	}

	@GetMapping("/titles")
	public List<ComicEntity> uniqueTitles() {
		logger.info("Titles and publishers endpoint");

		return comicService.getTitlesAndPublishers();
	}
	
	@PostMapping("/addComics")
	public ResponseEntity<?> addComics(@RequestBody List<ComicModel> comics) {
		logger.info("addComics " + comics);
		
		Map<ComicModel, String> errorMap = comicService.addComicsList(comics);
		
		if(errorMap.size() > 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
		}
		
		return ResponseEntity.ok(new Response("All comics added successfully."));
	}
}
