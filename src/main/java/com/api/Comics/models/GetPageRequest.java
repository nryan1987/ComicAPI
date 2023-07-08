package com.api.Comics.models;

import lombok.Data;

@Data
public class GetPageRequest {
    private int pageNumber;
    private int pageSize;
    private String searchTerm;
}
