package com.api.Comics.models;

import lombok.Data;

import java.util.List;

@Data
public class ComicPageResponse extends Response {
    int totalPages;
    int pageNumber;
    int pageSize;
    List<ComicPageItemModel> comics;
}
