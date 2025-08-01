package com.api.Comics.service;

import com.api.Comics.controllers.ComicController;
import com.api.Comics.entities.ComicEntity;
import com.api.Comics.entities.NoteEntity;
import com.api.Comics.entities.PublisherEntity;
import com.api.Comics.helper.ComicHelper;
import com.api.Comics.models.*;
import com.api.Comics.models.response.TitleData;
import com.api.Comics.models.response.TitlesAndPublishersResponse;
import com.api.Comics.repository.ComicRepository;
import com.api.Comics.repository.NoteRepository;
import com.api.Comics.repository.PublisherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ComicService {
    Logger logger = LoggerFactory.getLogger(ComicController.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ComicHelper comicHelper;
    @Autowired
    PublisherService publisherService;
    @Autowired
    ComicRepository comicRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    ObjectMapper objectMapper;

    public List<ComicEntity> latestIssues(int numIssues) {
        return comicRepository.findLatestIssues(numIssues);
    }

    public ResponseEntity<FindByTitleResponse> findByTitle(String title) {
        FindByTitleResponse response = new FindByTitleResponse();
        try {
            List<ComicEntity> comicEntities = comicRepository.findByTitle(title);
            response.setComics(comicEntities.stream().map(comicEntity -> objectMapper.convertValue(comicEntity, ComicModel.class)).collect(Collectors.toList()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Error finding issues by title.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public CollectionStats getCollectionStats() {
        CollectionStats colStats = new CollectionStats(comicRepository.getCollectionStats());
        return colStats;
    }

    public ResponseEntity<SingleComicResponse> getComic(int id) {
        SingleComicResponse response = new SingleComicResponse();
        Optional<ComicEntity> comicEntity = comicRepository.findById(id);
        if (comicEntity.isPresent()) {
            response = objectMapper.convertValue(comicEntity.get(), SingleComicResponse.class);
            response.setNotes(comicEntity.get().getNotes());
            response.setRecordCreationDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(comicEntity.get().getRecordCreationDate().getTime()), ZoneId.of("UTC")).format(formatter));
            response.setLastUpdated(LocalDateTime.ofInstant(Instant.ofEpochMilli(comicEntity.get().getLastUpdated().getTime()), ZoneId.of("UTC")).format(formatter));
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("No comic with id=" + id + " found.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public TitlesAndPublishersResponse getTitlesAndPublishers() {
        List<Object[]> lst = comicRepository.getTitlesAndPublishers();
        Map<String, TitleData> titlePublishersMap = new HashMap<>();

        for (Object[] obj : lst) {
            String title = (String) obj[0];
            String publisher = (String) obj[2];

            TitleData titleData = new TitleData();
            titleData.setTitle(title);
            titleData.setVolume((Integer) obj[1]);
            titleData.setPublishers(Stream.of(publisher).collect(Collectors.toSet()));

            titlePublishersMap.computeIfPresent(title, (key, value) -> {
                value.getPublishers().add(publisher);
                return value;
            });
            titlePublishersMap.putIfAbsent(title, titleData);
        }

        TitlesAndPublishersResponse response = new TitlesAndPublishersResponse();
        response.setTitleData(new ArrayList<>(titlePublishersMap.values()));
        response.setPublishers(publisherRepository.findAll().stream().map(PublisherEntity::getPublisher).collect(Collectors.toSet()));

        return response;
    }

    public List<String> getDistinctTitles() {
        logger.info("Querying for distinct titles...");
        return comicRepository.getDistinctTitles();
    }

    public synchronized List<ResponseError> addComicsList(List<ComicModel> lst) {
        try {
            Set<String> publishers = new HashSet<>();
            AtomicInteger ID = new AtomicInteger(comicRepository.getMaxComicID());
            logger.info("MAX ID: " + ID);
            LocalDate today = LocalDate.now();
            List<ComicEntity> entities = lst.stream().map(comicModel -> {
                ComicEntity comicEntity = objectMapper.convertValue(comicModel, ComicEntity.class);

                //Set default values
                comicEntity.setComicID(ID.incrementAndGet());
                comicEntity.setPublicationDate(LocalDate.of(today.getYear(), today.getMonthValue(), 1));
                comicEntity.setCondition("MT 10.0");
                publishers.add(comicEntity.getPublisher());

                //Set Notes
                List<NoteEntity> noteEntities = comicModel.getNotes() == null ? new ArrayList<>() : comicModel.getNotes();
                noteEntities.forEach(noteEntity -> noteEntity.setComicID(ID.get()));
                comicEntity.setNotes(noteEntities);

                return comicEntity;
            }).collect(Collectors.toList());
            entities.forEach(comicEntity -> logger.info("Comic Entity: {}", comicEntity));

            publisherService.addPublisher(new ArrayList<>(publishers));
            comicRepository.saveAll(entities);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public synchronized ResponseEntity<SingleComicResponse> updateComic(UpdateComicRequest updateComicRequest) {
        SingleComicResponse response = new SingleComicResponse();
        logger.info("saving comic: " + updateComicRequest);
        try {
            Optional<ComicEntity> comicEntity = comicRepository.findById(updateComicRequest.getComicID());
            if (comicEntity.isEmpty()) {
                response.setMessage("No comic with id=" + updateComicRequest.getComicID() + " found.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            Timestamp recordCreationTS = comicEntity.get().getRecordCreationDate();
            publisherService.addPublisher(List.of(updateComicRequest.getPublisher())); //This will ensure the publisher has been added to the publisher table before saving the comic entity.

            ComicEntity comicEntityToSave = objectMapper.convertValue(updateComicRequest, ComicEntity.class);
            comicRepository.save(comicEntityToSave);

            if (updateComicRequest.getDeletedNotes() != null) {
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

    public ResponseEntity<ComicPageResponse> getComicsPage(int pageNumber, int pageSize, String searchTerm) {
        logger.info("pageNumber: " + pageNumber);
        logger.info("pageSize: " + pageSize);
        logger.info("searchTerm: " + searchTerm);

        ComicPageResponse response = new ComicPageResponse();
        Page<ComicEntity> resultsPage;
        try {
            Pageable comicPage = PageRequest.of(pageNumber, pageSize);
            if (searchTerm == null || searchTerm.isEmpty()) {
                resultsPage = comicRepository.findAllWithNotes(comicPage);
            } else {
                resultsPage = comicRepository.getAllComicsSearchTerm(searchTerm.trim(), comicPage);
            }

            List<ComicPageItemModel> comics = new ArrayList<>();
            for (ComicEntity comicEntity : resultsPage.getContent()) {
                comics.add(comicHelper.convertComicEntityToComicPageItem(comicEntity));
            }

            response.setTotalPages(resultsPage.getTotalPages());
            response.setPageNumber(pageNumber);
            response.setPageSize(resultsPage.getSize());
            response.setComics(comics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error getting comic page: {}", e.getMessage());
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
