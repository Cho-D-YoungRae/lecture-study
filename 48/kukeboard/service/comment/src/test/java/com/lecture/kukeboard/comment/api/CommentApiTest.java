package com.lecture.kukeboard.comment.api;

import com.lecture.kukeboard.comment.service.request.CommentCreateRequest;
import com.lecture.kukeboard.comment.service.request.CommentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class CommentApiTest {

    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(
                1L,
                "my comment 1",
                null,
                1L
        ));
        CommentResponse response2 = createComment(new CommentCreateRequest(
                1L,
                "my comment 2",
                response1.commentId(),
                1L
        ));
        CommentResponse response3 = createComment(new CommentCreateRequest(
                1L,
                "my comment 3",
                response1.commentId(),
                1L
        ));

        System.out.println("commentID=" + response1.commentId());
        System.out.println("- commentID=" + response2.commentId());
        System.out.println("- commentID=" + response3.commentId());
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    /*
     * commentID=210281646229245952
     * - commentID=210281646518652928
     * - commentID=210281646573178880
     */
    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 210281646229245952L)
                .retrieve()
                .body(CommentResponse.class);
        System.out.println(response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v1/comments/{commentId}", 210281646573178880L)
                .retrieve()
                .body(Void.class);
    }
}
