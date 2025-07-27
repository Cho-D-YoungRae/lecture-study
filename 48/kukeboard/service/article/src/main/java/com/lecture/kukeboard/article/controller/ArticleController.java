package com.lecture.kukeboard.article.controller;

import com.lecture.kukeboard.article.service.ArticleService;
import com.lecture.kukeboard.article.service.request.ArticleCreateRequest;
import com.lecture.kukeboard.article.service.request.ArticleUpdateRequest;
import com.lecture.kukeboard.article.service.response.ArticlePageResponse;
import com.lecture.kukeboard.article.service.response.ArticleResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/v1/articles/{articleId}")
    public ArticleResponse read(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    @GetMapping("/v1/articles")
    public ArticlePageResponse readAll(
            @RequestParam Long boardId,
            @RequestParam Long page,
            @RequestParam Long pageSize) {
        return articleService.readAll(boardId, page, pageSize);
    }

    @GetMapping("/v1/articles/infinite-scroll")
    public List<ArticleResponse> readAllInfiniteScroll(
            @RequestParam Long boardId,
            @RequestParam Long pageSize,
            @RequestParam(required = false) @Nullable Long lastArticleId
    ) {
        return articleService.readAllInfiniteScroll(boardId, pageSize, lastArticleId);
    }

    @PostMapping("/v1/articles")
    public ArticleResponse create(@RequestBody ArticleCreateRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request) {
        return articleService.update(articleId, request);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }
}
