package com.api.Comics.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.Comics.controllers.ComicController;
import com.api.Comics.entities.ComicEntity;
import com.api.Comics.entities.NoteEntity;
import com.api.Comics.models.CollectionStats;
import com.api.Comics.models.ComicModel;
import com.api.Comics.repository.ComicRepository;
import com.api.Comics.repository.NoteRepository;

@Service
public class ComicService {
	Logger logger = LoggerFactory.getLogger(ComicController.class);

	@Autowired
	private ComicRepository comicRepository;
	
	@Autowired NoteRepository noteRepository;

	public List<ComicEntity> latestIssues(int numIssues) {
		return comicRepository.findLatestIssues(numIssues);
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
			c.setPublisher((String)obj[1]);
			
			comicLst.add(c);
		}
		
		return comicLst;
	}
	
	public synchronized Map<ComicModel, String> addComicsList(List<ComicModel> lst) {
		HashMap<ComicModel, String> returnMap = new HashMap<>();
		Integer ID = comicRepository.getMaxComicID();
		logger.info("MAX ID: " + ID);
	
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		LocalDate today = LocalDate.now();
		for(ComicModel c : lst) {
			c.setComicID(++ID);
			c.setCondition("MT 10.0");
			c.setPublicationDate(LocalDate.of(today.getYear(), today.getMonthValue(), 1));
			c.setRecordCreationDate(ts);
			c.setLastUpdated(ts);
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
				returnMap.put(c, e.getMessage());
			}
		}
		
		return returnMap;
	}
}
