package com.lecture.kukeboard.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Table(name = "article")
@Entity
public class Article {

    @Id
    private Long articleId;
    private String title;
    private String content;
    private Long boardId; // shard key
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    protected Article() {
    }

    public static Article create(Long id, String title, String content, Long boardId, Long writerId) {
        Article article = new Article();
        article.articleId = id;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.writerId = writerId;
        LocalDateTime now = LocalDateTime.now();
        article.createdAt = now;
        article.modifiedAt = now;
        return article;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getBoardId() {
        return boardId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
