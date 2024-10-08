-- Payment

drop table if exists payment_order_histories;
drop table if exists payment_orders;
drop table if exists payment_events;

create table payment_events
(
    id              bigint auto_increment primary key,
    buyer_id        bigint       not null,
    is_payment_done boolean      not null default false,
    payment_key     varchar(255) unique,
    order_id        varchar(255) unique,
    type            varchar(45)  not null,  -- default 'NORMAL'
    order_name      varchar(255) not null,
    method          varchar(45),    -- default 'EASY_PAY'
    psp_raw_data    json,
    approved_at     datetime,
    created_at      datetime     not null default current_timestamp,
    updated_at      datetime     not null default current_timestamp
);

create table payment_orders
(
    id                   bigint auto_increment primary key,
    payment_event_id     bigint         not null,
    seller_id            bigint         not null,
    product_id           bigint         not null,
    order_id             varchar(255)   not null,
    amount               decimal(12, 2) not null,
    payment_order_status varchar(45)    not null, -- default  'NOT_STARTED',
    ledger_updated       boolean        not null default false,
    wallet_updated       boolean        not null default false,
    failed_count         tinyint        not null default 0,
    threshold_count      tinyint        not null default 5,
    created_at           datetime       not null default current_timestamp,
    updated_at           datetime       not null default current_timestamp
);

create table payment_order_histories
(
    id               bigint auto_increment primary key,
    payment_order_id bigint       not null,
    previous_status  varchar(45),
    new_status       varchar(45),
    created_at       datetime     not null default current_timestamp,
    changed_by       varchar(255),
    reason           varchar(255)
);