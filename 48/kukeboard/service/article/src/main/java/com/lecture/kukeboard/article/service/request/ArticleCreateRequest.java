package com.lecture.kukeboard.article.service.request;

public record ArticleCreateRequest(
        String title,
        String content,
        Long writerId,
        Long boardId
) {
}
