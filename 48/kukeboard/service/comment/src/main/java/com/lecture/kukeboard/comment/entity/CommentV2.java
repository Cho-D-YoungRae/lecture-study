package com.lecture.kukeboard.comment.entity;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comment_v2")
public class CommentV2 {

    @Id
    private Long commentId;
    private String content;
    private Long articleId;
    private Long writerId;
    @Embedded
    private CommentPath commentPath;
    private boolean deleted;
    private LocalDateTime createdAt;

}
