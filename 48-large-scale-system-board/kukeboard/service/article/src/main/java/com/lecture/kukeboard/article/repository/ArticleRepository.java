package com.lecture.kukeboard.article.repository;

import com.lecture.kukeboard.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = """
            select
                a.article_id,
                a.title,
                a.content,
                a.board_id,
                a.writer_id,
                a.created_at,
                a.modified_at
            from (
                select article_id
                from article
                where board_id = :boardId
                order by article_id desc
                limit :limit offset :offset
            ) t
            left join article a on t.article_id = a.article_id
    """, nativeQuery = true)
    List<Article> findAll(
            @Param("boardId") Long boardId,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

    @Query(value = """
            select count(*)
            from (
                select article_id
                from article
                where board_id = :boardId
                order by article_id desc
                limit :limit
            ) t
    """, nativeQuery = true)
    Long count(@Param("boardId") Long boardId, @Param("limit") Long limit);

    @Query(value = """
            select
                a.article_id,
                a.title,
                a.content,
                a.board_id,
                a.writer_id,
                a.created_at,
                a.modified_at
            from article a
            where a.board_id = :boardId
            order by article_id desc
            limit :limit
    """, nativeQuery = true)
    List<Article> findAllInfiniteScroll(
            @Param("boardId") Long boardId,
            @Param("limit") Long limit
    );

    @Query(value = """
            select
                a.article_id,
                a.title,
                a.content,
                a.board_id,
                a.writer_id,
                a.created_at,
                a.modified_at
            from article a
            where a.board_id = :boardId and a.article_id < :lastArticleId
            order by a.article_id desc
            limit :limit
    """, nativeQuery = true)
    List<Article> findAllInfiniteScroll(
            @Param("boardId") Long boardId,
            @Param("lastArticleId") Long lastArticleId,
            @Param("limit") Long limit
    );
}
