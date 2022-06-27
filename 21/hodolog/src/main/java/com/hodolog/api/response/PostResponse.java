package com.hodolog.api.response;

import com.hodolog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.*;

@Getter
public class PostResponse {

    private final Long id;

    private final String title;

    private final String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, min(title.length(), 10));
        this.content = content;
    }
}
