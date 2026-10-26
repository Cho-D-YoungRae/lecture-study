package com.lecture.kukeboard.article.service.response;

import com.lecture.kukeboard.article.entity.Article;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        Long boardId,
        Long writerId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getArticleId(),
                article.getTitle(),
                article.getContent(),
                article.getBoardId(),
                article.getWriterId(),
                article.getCreatedAt(),
                article.getModifiedAt()
        );
    }
}
