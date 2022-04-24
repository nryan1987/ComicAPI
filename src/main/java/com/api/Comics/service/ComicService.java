package com.api.Comics.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.Comics.controllers.ComicController;
import com.api.Comics.entities.ComicEntity;
import com.api.Comics.entities.NoteEntity;
import com.api.Comics.entities.PublisherEntity;
import com.api.Comics.models.CollectionStats;
import com.api.Comics.models.ComicModel;
import com.api.Comics.models.ResponseError;
import com.api.Comics.repository.ComicRepository;
import com.api.Comics.repository.NoteRepository;
import com.api.Comics.repository.PublisherRepository;

@Service
public class ComicService {
	Logger logger = LoggerFactory.getLogger(ComicController.class);

	@Autowired
	private ComicRepository comicRepository;
	
	@Autowired NoteRepository noteRepository;
	@Autowired PublisherRepository publisherRepository;

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
				comicRepository.save(c.getComicEntity());
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
