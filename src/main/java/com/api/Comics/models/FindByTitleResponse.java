package com.api.Comics.models;

import lombok.Data;

import java.util.List;

@Data
public class FindByTitleResponse extends Response {
    private List<ComicModel> comics;
}
