package com.lecture.kukeboard.article;

import com.lecture.kukeboard.article.service.request.ArticleCreateRequest;
import com.lecture.kukeboard.article.service.request.ArticleUpdateRequest;
import com.lecture.kukeboard.article.service.response.ArticlePageResponse;
import com.lecture.kukeboard.article.service.response.ArticleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

class ArticleApiTest {

    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleCreateRequest request = new ArticleCreateRequest(
                "hi",
                "my content",
                1L,
                1L
        );
        create(request);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(207282911408128000L);
        System.out.println(response);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateTest() {
        ArticleUpdateRequest request = new ArticleUpdateRequest(
                "updated",
                "updated"
        );
        ArticleResponse response = update(207282911408128000L, request);
        System.out.println(response);
    }

    ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void deleteTest() {
        delete(207282911408128000L);
    }

    void delete(Long articleId) {
        restClient.delete()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(Void.class);
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&pageSize=30&page=50000")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response.articles().size() = " + response.articles().size());
    }

    @Test
    void readAllInfiniteScrollTest() {

        List<ArticleResponse> articles1 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=30")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        System.out.println("articles1.size() = " + articles1.size());

        Long lastArticleId = articles1.getLast().id();
        List<ArticleResponse> articles2 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=30&lastArticleId={lastArticleId}", lastArticleId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });
        System.out.println("lastArticleId = " + lastArticleId);
        System.out.println("articles2.size() = " + articles2.size());
    }

}
