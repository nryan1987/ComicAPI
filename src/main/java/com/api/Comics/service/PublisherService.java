package com.api.Comics.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.Comics.entities.PublisherEntity;
import com.api.Comics.models.PublisherListResponse;
import com.api.Comics.repository.PublisherRepository;

@Service
public class PublisherService {
	Logger logger = LoggerFactory.getLogger(PublisherService.class);

	@Autowired
	PublisherRepository publisherRepository;
		
	public ResponseEntity<PublisherListResponse> getPublishers() {
		PublisherListResponse response;
		try {
			Iterable<PublisherEntity> publisherEntities = publisherRepository.findAll();
			Iterator<PublisherEntity> publisherIterator = publisherEntities.iterator();
			
			List<String> publisherStrings = new ArrayList<>(); 
			while (publisherIterator.hasNext()) {
				PublisherEntity pe = publisherIterator.next();
				publisherStrings.add(pe.getPublisher());
			}
			response = new PublisherListResponse("Publisher query successful.");
			response.setPublishers(publisherStrings);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response = new PublisherListResponse(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	public String addPublisher(String newPublisher) {
		newPublisher = newPublisher.trim();
		List<String> publisherLst = new ArrayList<>();
		Iterable<PublisherEntity> publishers = publisherRepository.findAll();
		publishers.forEach(publisher -> { publisherLst.add(publisher.getPublisher()); });
		
		for(String publisher : publisherLst) {
			if(publisher.equalsIgnoreCase(newPublisher)) {
				return publisher;
			}
		}
		
		int publisherID = publisherRepository.getMaxPublisherID().intValue();
		
		PublisherEntity publisherEntity = new PublisherEntity();
		publisherEntity.setPublisher(newPublisher);
		publisherEntity.setPublisherID(++publisherID);
		
		return publisherRepository.save(publisherEntity).getPublisher();
	}
}
