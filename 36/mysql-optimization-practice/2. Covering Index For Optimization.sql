create table orders
(
    order_id     bigint auto_increment
        primary key,
    customer_id  bigint         not null,
    order_date   date           not null,
    total_amount decimal(10, 2) not null
);

create index idx_costumer_order_date on orders (customer_id, order_date);

create index idx_covering on orders (customer_id, order_date, total_amount);
drop index idx_covering on orders;

select *
from orders
where customer_id = 25675
order by order_date desc
limit 10
;

explain
select *
from orders
where customer_id = 25675
order by order_date desc
limit 10
;

explain
analyze
select *
from orders
where customer_id = 25675
order by order_date desc
limit 10
;