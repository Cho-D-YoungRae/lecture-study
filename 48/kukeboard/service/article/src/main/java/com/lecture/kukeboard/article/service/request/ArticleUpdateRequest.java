package com.lecture.kukeboard.article.service.request;

public record ArticleUpdateRequest(
        String title,
        String content
) {
}
