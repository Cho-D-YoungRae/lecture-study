package com.lecture.kukeboard.article.repository;

import com.lecture.kukeboard.article.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    void findAllTest() {
        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);
        System.out.println("articles.size() = " + articles.size());
    }

    @Test
    void countTest() {
        Long count = articleRepository.count(1L, 10000L);
        System.out.println("count = " + count);
    }

    @Test
    void findInfiniteScrollTest() {
        List<Article> articles1 = articleRepository.findAllInfiniteScroll(1L, 30L);
        System.out.println("articles1.size() = " + articles1.size());

        Long lastArticleId = articles1.getLast().getArticleId();
        List<Article> articles2 = articleRepository.findAllInfiniteScroll(1L, lastArticleId, 30L);
        System.out.println("articles2.size() = " + articles2.size());
    }
}