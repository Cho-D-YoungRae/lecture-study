-- 2-1

CREATE TABLE product
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100),
    price      DECIMAL(10, 2),
    created_at DATETIME
);

select count(*) from product;

select *
from product
order by created_at desc, price asc
limit 10
;

explain
select *
from product
order by created_at desc, price asc
limit 10
;

explain analyze
select *
from product
order by created_at desc, price asc
limit 10
;

create index idx_created_at_price on product(created_at desc, price asc);

-- 2-2
