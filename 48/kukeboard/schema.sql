create database article;

create table article.article
(
    article_id  bigint        not null comment 'Shard Key',
    title       varchar(100)  not null,
    content     varchar(3000) not null,
    board_id    bigint        not null,
    writer_id   bigint        not null,
    created_at  timestamp     not null,
    modified_at timestamp     not null,
    primary key (article_id)
);

create index idx_board_id_article_id on article.article (board_id asc, article_id desc);

create database comment;

create table comment.comment
(
    comment_id        bigint        not null,
    content           varchar(3000) not null,
    article_id        bigint        not null comment 'Shard Key',
    parent_comment_id bigint        not null,
    writer_id         bigint        not null,
    deleted           boolean       not null,
    created_at        datetime      not null,
    primary key (comment_id)
);

create index idx_article_id_parent_comment_id_comment_id on comment.comment (article_id asc, parent_comment_id asc, comment_id desc);