package com.api.Comics.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.Comics.models.PublisherListResponse;
import com.api.Comics.service.PublisherService;
import com.api.Comics.constants.*;

@RestController
@CrossOrigin
@RequestMapping(APIConstants.PUBLISHER)
public class PublisherController {
	@Autowired
	PublisherService publisherService;
	
	@GetMapping(APIConstants.GET_PUBLISHERS)
	public ResponseEntity<PublisherListResponse> getPublishers() {
		return publisherService.getPublishers();
	}

}
