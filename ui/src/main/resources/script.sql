drop database if exists inventory_management;
create database if not exists inventory_management;

use inventory_management;

create table users
(
    id         int auto_increment not null primary key,
    first_name varchar(50),
    last_name  varchar(50),
    email      varchar(100) unique,
    password   varchar(100),
    constraint unique_email unique (email)
);

create table categories
(
    id          int auto_increment not null primary key,
    name        varchar(50) unique,
    description varchar(100)
);

create table products
(
    id          int auto_increment not null primary key,
    name        varchar(50),
    description varchar(100),
    price       DOUBLE,
    quantity    int,
    category_id int,
    user_id     int,
    constraint fk_user foreign key (user_id) references users (id),
    constraint fk_category foreign key (category_id) references categories (id)
);

-- Insert into User
insert into users (first_name, last_name, email, password)
values ('John', 'Doe', 'john.doe@example.com', 'password123');

-- Insert into Category
insert into categories (name, description)
values ('Electronics', 'Devices and gadgets');

-- Insert into Product
insert into products (name, description, price, quantity, category_id, user_id)
values ('Smartphone', 'Latest model', 799.99, 100, 1, 1);


select *
from categories;

select *
from products where category_id = 1;

