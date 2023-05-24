package com.api.Comics.controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.api.Comics.models.ResponseError;
import com.api.Comics.models.SingleComicResponse;
import com.api.Comics.models.UpdateComicRequest;
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
	
	@PostMapping("/getComicsPage/{pageNumber}/{pageSize}")
	public String getComicsPage(@PathVariable int pageNumber,
			@PathVariable int pageSize,
			@RequestBody(required = false) String searchTerm) {
		logger.info("pageNumber: " + pageNumber);
		logger.info("pageSize: " + pageSize);
		logger.info("searchTerm: " + searchTerm);
		Response r;
		Page<ComicEntity> comicPage = comicService.getComicsPage(pageNumber, pageSize, searchTerm);
		
		HttpStatus status = HttpStatus.OK;
		JSONObject returnJson = new JSONObject();
		
		if(comicPage.getSize() == 0) {
			status = HttpStatus.EXPECTATION_FAILED;
			returnJson.put("message", "No comics in page " + pageNumber);
		}
		else {
			logger.info("returning {} comics", comicPage.getNumberOfElements());
			JSONArray arr = new JSONArray();
			comicPage.forEach(c -> {
				JSONObject jo = new JSONObject();
				jo.put("comicID", c.getComicID());
				jo.put("title", c.getTitle());
				jo.put("volume", c.getVolume());
				jo.put("issue", c.getIssue());
				jo.put("pricePaid", c.getPricePaid());
				jo.put("value", c.getValue());
				jo.put("publicationDate", c.getPublicationDate());
				jo.put("publisher", c.getPublisher());
				jo.put("condition", c.getCondition());
				arr.put(jo);
			});
			returnJson.put("value", arr);
			returnJson.put("totalPages", comicPage.getTotalPages());
			returnJson.put("message", "Page query successful.");
		}
		
		returnJson.put("status", status);
		
		return returnJson.toString();
	}
}
