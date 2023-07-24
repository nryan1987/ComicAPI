package com.api.Comics.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComicPageItemModel {
    private int ComicID, volume;
    private double issue, pricePaid, value;
    private String title, publisher, condition, publicationDate, notesStr;
}
