create database article;

create table article.article
(
    article_id bigint        not null,
    title      varchar(100)  not null,
    content    varchar(3000) not null,
    board_id   bigint        not null,
    writer_id  bigint        not null,
    created_at timestamp     not null,
    modified_at timestamp     not null,
    primary key (article_id)
);

create index idx_board_id_article_id on article.article (board_id asc, article_id desc);
