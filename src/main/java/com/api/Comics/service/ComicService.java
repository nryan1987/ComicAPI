package com.api.Comics.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.Comics.controllers.ComicController;
import com.api.Comics.entities.ComicEntity;
import com.api.Comics.entities.NoteEntity;
import com.api.Comics.entities.PublisherEntity;
import com.api.Comics.models.CollectionStats;
import com.api.Comics.models.ComicModel;
import com.api.Comics.models.ResponseError;
import com.api.Comics.models.SingleComicResponse;
import com.api.Comics.models.UpdateComicRequest;
import com.api.Comics.repository.ComicRepository;
import com.api.Comics.repository.NoteRepository;
import com.api.Comics.repository.PublisherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ComicService {
	Logger logger = LoggerFactory.getLogger(ComicController.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ComicRepository comicRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired NoteRepository noteRepository;
	@Autowired PublisherRepository publisherRepository;
	@Autowired PublisherService publisherService;

	public List<ComicEntity> latestIssues(int numIssues) {
		return comicRepository.findLatestIssues(numIssues);
	}
	
	public List<ComicEntity> findByTitle(String title) {
		return comicRepository.findByTitle(title);
	}
	
	public CollectionStats getCollectionStats() {
		CollectionStats colStats = new CollectionStats(comicRepository.getCollectionStats());
		return colStats;
	}
	
	public ResponseEntity<SingleComicResponse> getComic(int id) {		
		SingleComicResponse response = new SingleComicResponse();
		Optional<ComicEntity> comicEntity = comicRepository.findById(id);
		if(comicEntity.isPresent()) {
			response = objectMapper.convertValue(comicEntity.get(), SingleComicResponse.class);
			response.setNotes(noteRepository.findNotesByComicID(id));
			response.setRecordCreationDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(comicEntity.get().getRecordCreationDate().getTime()), ZoneId.of("UTC")).format(formatter));
			response.setLastUpdated(LocalDateTime.ofInstant(Instant.ofEpochMilli(comicEntity.get().getLastUpdated().getTime()), ZoneId.of("UTC")).format(formatter));
			return ResponseEntity.ok(response);
		} else {
			response.setMessage("No comic with id=" + id + " found.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	public List<ComicEntity> getTitlesAndPublishers() {
		List<Object[]> lst = comicRepository.getTitlesAndPublishers(); 
		
		ArrayList<ComicEntity> comicLst = new ArrayList<>();
		
		for(Object[] obj : lst) {
			ComicEntity c = new ComicEntity();
			c.setTitle((String)obj[0]);
			c.setVolume((Integer)obj[1]);
			c.setPublisher((String)obj[2]);
			
			comicLst.add(c);
		}
		
		return comicLst;
	}
	
	public List<String> getDistinctTitles() {
		logger.info("Querying for distinct titles...");
		return comicRepository.getDistinctTitles();
	}
	
	public synchronized List<ResponseError> addComicsList(List<ComicModel> lst) {
		List<ResponseError> errors = new ArrayList<>();
		Integer ID = comicRepository.getMaxComicID();
		logger.info("MAX ID: " + ID);

		Iterable<PublisherEntity> publishers = publisherRepository.findAll();
		ArrayList<String> publishersStr = new ArrayList<>();
		int publisherID = publisherRepository.getMaxPublisherID().intValue();

		LocalDate today = LocalDate.now();
		for(ComicModel c : lst) {

			publishers.forEach(publisher -> { publishersStr.add(publisher.getPublisher()); });
			boolean containsSearchStr = publishersStr.stream().anyMatch(c.getPublisher()::equalsIgnoreCase);

			if(!containsSearchStr) {
				PublisherEntity pe = new PublisherEntity();
				pe.setPublisherID(++publisherID);
				pe.setPublisher(c.getPublisher());
				publisherRepository.save(pe);

				publishersStr.add(c.getPublisher());
			}

			c.setComicID(++ID);
			c.setCondition("MT 10.0");
			c.setPublicationDate(LocalDate.of(today.getYear(), today.getMonthValue(), 1));
			if(c.getNotes() == null) {
				c.setNotes(new ArrayList<NoteEntity>());
			}
		
			logger.info(c.toString());
			try {
				comicRepository.save(objectMapper.convertValue(c, ComicEntity.class));
				for(NoteEntity note : c.getNotes()) {
					note.setComicID(ID);
					logger.info(note.toString());
					noteRepository.save(note);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				errors.add(new ResponseError(c.getShortString(), e.getMessage()));
			}
		}
		
		return errors;
	}
	
	public synchronized ResponseEntity<SingleComicResponse> updateComic(UpdateComicRequest updateComicRequest) {
		SingleComicResponse response = new SingleComicResponse();
		logger.info("saving comic: " + updateComicRequest);
		try {
			Optional<ComicEntity> comicEntity = comicRepository.findById(updateComicRequest.getComicID());
			if(comicEntity.isEmpty()) {
				response.setMessage("No comic with id=" + updateComicRequest.getComicID() + " found.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			
			Timestamp recordCreationTS = comicEntity.get().getRecordCreationDate();
			
			ComicEntity comicEntityToSave = objectMapper.convertValue(updateComicRequest, ComicEntity.class);
			comicEntityToSave.setPublisher(publisherService.addPublisher(updateComicRequest.getPublisher()));
			comicRepository.save(comicEntityToSave);
			noteRepository.saveAll(updateComicRequest.getNotes());

			if(updateComicRequest.getDeletedNotes() != null) {
				updateComicRequest.getDeletedNotes().forEach(noteEntity -> {
					logger.info("deleting note: " + noteEntity);
					noteRepository.deleteById(noteEntity.getNoteID());
				});
			}
			
			ZonedDateTime todayCDT = LocalDateTime.now().atZone(ZoneId.of("America/Chicago"));
			
			response = objectMapper.convertValue(comicEntityToSave, SingleComicResponse.class);
			response.setNotes(noteRepository.findNotesByComicID(updateComicRequest.getComicID()));
			response.setRecordCreationDate(recordCreationTS.toLocalDateTime().atZone(ZoneId.of("UTC")).format(formatter));
			response.setLastUpdated(todayCDT.format(formatter));
			response.setMessage("Comic " + updateComicRequest.getComicID() + " successfully updated.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response = new SingleComicResponse(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	public Page<ComicEntity> getComicsPage(int pageNumber, int pageSize, String searchTerm) {
		logger.info("pageNumber: " + pageNumber);
		logger.info("pageSize: " + pageSize);
		logger.info("searchTerm: " + searchTerm);
		
		Pageable comicPage = PageRequest.of(pageNumber, pageSize);
		
		if(searchTerm == null || searchTerm.isEmpty()) {
			logger.info("null searchTerm");
			return comicRepository.getAllComics(comicPage);
		}
		else {
			logger.info("not null searchTerm");
			return comicRepository.getAllComicsSearchTerm(searchTerm, comicPage);
		}
	}
}
