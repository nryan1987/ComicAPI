package com.api.Comics.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.Comics.models.Response;

@RestController
@CrossOrigin
@RequestMapping("/app")
public class AppController {
	Logger logger = LoggerFactory.getLogger(AppController.class);
	
	@GetMapping("/healthCheck")
	public Response healthCheck() {
		logger.info("Health check");
		return new Response("Running.");
	}

}
