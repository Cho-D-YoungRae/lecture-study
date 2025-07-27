package com.lecture.kukeboard.article.service.response;

import java.util.List;

public record ArticlePageResponse(
        List<ArticleResponse> articles,
        Long articleCount
) {

    public static ArticlePageResponse from(List<ArticleResponse> articles, Long articleCount) {
        return new ArticlePageResponse(articles, articleCount);
    }
}
