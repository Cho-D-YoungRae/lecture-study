create table company
(
    id      bigint auto_increment,
    name    varchar(128),
    address varchar(128),
    primary key (id)
);

create index on company(name);

create table employee
(
    id         bigint auto_increment,
    company_id bigint,
    name       varchar(128),
    address    varchar(128),
    primary key (id)
);

create index on employee(name);

alter table employee
    add constraint employee_company_fk
    foreign key (id)
    references company;
