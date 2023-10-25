package com.api.Comics.helper;

import com.api.Comics.entities.ComicEntity;
import com.api.Comics.entities.NoteEntity;
import com.api.Comics.models.ComicModel;
import com.api.Comics.models.ComicPageItemModel;
import com.api.Comics.models.ResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ComicHelper {
    Logger logger = LoggerFactory.getLogger(ComicHelper.class);
    @Autowired
    ObjectMapper objectMapper;

    public List<ComicEntity> buildComicEntityList(List<ComicModel> lst, int maxID) {
        AtomicInteger ID = new AtomicInteger(maxID);
        LocalDate today = LocalDate.now();
        return lst.stream().map(comicModel -> {
            ComicEntity comicEntity = objectMapper.convertValue(comicModel, ComicEntity.class);

            //Set default values
            comicEntity.setComicID(ID.incrementAndGet());
            comicEntity.setPublicationDate(LocalDate.of(today.getYear(), today.getMonthValue(), 1));
            comicEntity.setCondition("MT 10.0");

            //Set Notes
            List<NoteEntity> noteEntities = comicModel.getNotes() == null ? new ArrayList<>() : comicModel.getNotes();
            noteEntities.forEach(noteEntity -> noteEntity.setComicID(ID.get()));
            comicEntity.setNotes(noteEntities);

            return comicEntity;
        }).collect(Collectors.toList());
    }

    public ComicPageItemModel convertComicEntityToComicPageItem(ComicEntity comicEntity) {
        ComicPageItemModel comicPageItemModel = objectMapper.convertValue(comicEntity, ComicPageItemModel.class);

        if (comicEntity.getNotes() != null && comicEntity.getNotes().size() > 0) {
            String notesStr = comicEntity.getNotes().stream().map(NoteEntity::getNotes).collect(Collectors.joining(", "));
            comicPageItemModel.setNotesStr(notesStr);
        }
        comicPageItemModel.setPublicationDate(getDisplayDate(comicEntity.getPublicationDate()));
        return comicPageItemModel;
    }

    private String getDisplayDate(LocalDate publicationDate) {
        switch (publicationDate.getDayOfMonth()) {
            case 20:
                return "Spring, " + publicationDate.getYear();
            case 21:
                return "Summer, " + publicationDate.getYear();
            case 22:
                return "Fall, " + publicationDate.getYear();
            case 23:
                return "Winter, " + publicationDate.getYear();
            case 30:
                return "Annual, " + publicationDate.getYear();
            case 31:
                return "Original Graphic Novel, " + publicationDate.getYear();
            default:
                return publicationDate.getMonth().name() + ", " + publicationDate.getYear();

        }
    }
}
