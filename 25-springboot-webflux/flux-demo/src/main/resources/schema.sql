drop table if exists customer;
create table customer
(
    id serial primary key,
    first_name varchar(255),
    last_name varchar(255),
    primary key (id)
);