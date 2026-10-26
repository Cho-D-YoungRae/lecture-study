package com.lecture.kukeboard.article.service;

import com.lecture.kukeboard.article.entity.Article;
import com.lecture.kukeboard.article.repository.ArticleRepository;
import com.lecture.kukeboard.article.service.request.ArticleCreateRequest;
import com.lecture.kukeboard.article.service.request.ArticleUpdateRequest;
import com.lecture.kukeboard.article.service.response.ArticlePageResponse;
import com.lecture.kukeboard.article.service.response.ArticleResponse;
import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.create(
                        snowflake.nextId(),
                        request.title(),
                        request.content(),
                        request.boardId(),
                        request.writerId()
                )
        );
        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.title(), request.content());
        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        return ArticleResponse.from(article);
    }

    @Transactional
    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        return ArticlePageResponse.from(
                articleRepository.findAll(
                                boardId,
                                (page - 1) * pageSize,
                                pageSize
                        ).stream()
                        .map(ArticleResponse::from)
                        .toList(),
                articleRepository.count(
                        boardId,
                        PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
                )
        );
    }

    public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize, @Nullable Long lastArticleId) {
        List<Article> articles = lastArticleId == null ?
                articleRepository.findAllInfiniteScroll(boardId, pageSize) :
                articleRepository.findAllInfiniteScroll(boardId, lastArticleId, pageSize);
        return articles.stream()
                .map(ArticleResponse::from)
                .toList();
    }
}
