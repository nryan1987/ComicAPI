package com.api.Comics.helper;

import com.api.Comics.entities.ComicEntity;
import com.api.Comics.entities.NoteEntity;
import com.api.Comics.models.ComicPageItemModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class ComicHelper {
    @Autowired
    ObjectMapper objectMapper;

    public ComicPageItemModel convertComicEntityToComicPageItem(ComicEntity comicEntity) {
        ComicPageItemModel comicPageItemModel = objectMapper.convertValue(comicEntity, ComicPageItemModel.class);

        if (comicEntity.getNotes() != null && comicEntity.getNotes().size() > 0) {
            String notesStr = comicEntity.getNotes().stream().map(NoteEntity::getNotes).collect(Collectors.joining(", "));
            comicPageItemModel.setNotes(notesStr);
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
