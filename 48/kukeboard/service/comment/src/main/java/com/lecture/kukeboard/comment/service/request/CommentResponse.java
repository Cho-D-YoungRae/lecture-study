package com.lecture.kukeboard.comment.service.request;

import com.lecture.kukeboard.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String content,
        Long parentCommentId,
        Long articleId,
        Long writerId,
        boolean deleted,
        LocalDateTime createdAt
) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getContent(),
                comment.getParentCommentId(),
                comment.getArticleId(),
                comment.getWriterId(),
                comment.isDeleted(),
                comment.getCreatedAt()
        );
    }
}
