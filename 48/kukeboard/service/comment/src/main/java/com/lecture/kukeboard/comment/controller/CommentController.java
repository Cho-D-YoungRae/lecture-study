package com.lecture.kukeboard.comment.controller;

import com.lecture.kukeboard.comment.service.CommentService;
import com.lecture.kukeboard.comment.service.request.CommentCreateRequest;
import com.lecture.kukeboard.comment.service.response.CommentResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse read(
            @PathVariable Long commentId
    ) {
        return commentService.read(commentId);
    }

    @PostMapping("/v1/comments")
    public CommentResponse create(@RequestBody CommentCreateRequest request) {
        return commentService.create(request);
    }

    @DeleteMapping("/v1/comments/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }
}
