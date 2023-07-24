package com.api.Comics.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

	/**
	 *
	 * @param newPublishers a list of strings
	 * @return the number of new publishers added.
	 */
	public int addPublisher(List<String> newPublishers) {
		List<String> publishers = publisherRepository.findAll().stream().map(publisherEntity -> publisherEntity.getPublisher().toUpperCase()).collect(Collectors.toList());

		List<String> publishersToAdd = new ArrayList<>();
		for(String newPublisher : newPublishers) {
			if(!publishers.contains(newPublisher.trim().toUpperCase())) {
				publishersToAdd.add(newPublisher.trim());
			}
		}

		if(publishersToAdd.isEmpty()) {
			return 0; //no new publishers added.
		} else {
			AtomicInteger publisherID = new AtomicInteger(publisherRepository.getMaxPublisherID());
			List<PublisherEntity> publisherEntities = publishersToAdd.stream().map(publisher->{
				PublisherEntity publisherEntity = new PublisherEntity();
				publisherEntity.setPublisher(publisher);
				publisherEntity.setPublisherID(publisherID.incrementAndGet());
				return publisherEntity;
			}).collect(Collectors.toList());
			return publisherRepository.saveAll(publisherEntities).size();
		}
	}
}
