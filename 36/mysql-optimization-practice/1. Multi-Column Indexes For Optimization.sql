use opt_practice;

create table orders
(
    order_id    bigint auto_increment
        primary key,
    customer_id bigint       not null,
    order_date  date         not null,
    product_id  bigint       not null,
    quantity    int          not null,
    status      varchar(255) not null
);

explain analyze
select * from orders
where customer_id = 3905
and order_date > '2023-01-01'
order by order_date desc
limit 10
;

create index idx_order_date_customer_id on orders(order_date, customer_id);

drop index idx_order_date_customer_id on orders;

create index idx_customer_id_order_date on orders(customer_id, order_date);

drop index idx_customer_id_order_date on orders;

analyze table orders update histogram on customer_id, status with 100 buckets;

use information_schema;
select * from COLUMN_STATISTICS where table_name = 'orders';

