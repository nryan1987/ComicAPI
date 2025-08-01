package com.api.Comics.models.response;

import com.api.Comics.models.Response;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TitlesAndPublishersResponse extends Response {
    List<TitleData> titleData;
    Set<String> publishers;
}
