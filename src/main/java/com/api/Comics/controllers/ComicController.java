package com.api.Comics.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.Comics.models.Comic;
import com.api.Comics.repository.ComicRepository;

@RestController
@RequestMapping("/comic")
public class ComicController {
	@Autowired
	private ComicRepository comicRepository;
	
	Logger logger = LoggerFactory.getLogger(ComicController.class);
	
	@GetMapping("/{id}")
	public Comic oneComic(@PathVariable int id) {
		logger.info("Comic controller " + id);

		Optional<Comic> c = comicRepository.findById(id);
		logger.info(c.get().toString());
		
		/*Comic c2 = comicRepository.findSingleIssue(id);
		logger.info(c2.getPicture() + " exists: " + c2.doesPictureExist());
		
		File picFile = new File(c2.getPicture());
		logger.info(picFile.getPath());
		logger.info(picFile.getAbsolutePath());
		
		logger.info("Equals: " + c2.equals(c.get()));*/
		
		return c.get();
	}
	
	@GetMapping("/all")
	public List<Comic> allComics() {
		logger.info("All comics endpoint");
		
		return (List<Comic>) comicRepository.findAll();
	}
	
	@GetMapping("/titles")
	public List<String> uniqueTitles(){
		logger.info("Unique titles endpoint");
		
		return comicRepository.findUniqueTitles();
	}
}
