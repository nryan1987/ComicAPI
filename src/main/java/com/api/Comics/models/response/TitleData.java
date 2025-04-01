package com.api.Comics.models.response;

import lombok.Data;

import java.util.Set;

@Data
public class TitleData {
    private String title;
    private Integer volume;
    private Set<String> publishers;
}
