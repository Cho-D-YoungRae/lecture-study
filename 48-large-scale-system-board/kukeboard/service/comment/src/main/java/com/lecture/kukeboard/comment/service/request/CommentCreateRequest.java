package com.lecture.kukeboard.comment.service.request;

public record CommentCreateRequest(
        Long articleId,
        String content,
        Long parentCommentId,
        Long writerId
) {
}
