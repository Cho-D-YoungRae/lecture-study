package com.hodolog.api.response;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.*;

@Getter
public class PostResponse {

    private final Long id;

    private final String title;

    private final String content;

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, min(title.length(), 10));
        this.content = content;
    }
}
