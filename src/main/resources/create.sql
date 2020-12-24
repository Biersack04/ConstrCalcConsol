create table if not exists Materials (
    id serial Primary key,
    name text,
    number int,
    price_for_one int
);

create table if not exists Customer (
    id serial Primary key,
    name text,
    surname text,
    mailbox text,
    telephone text
);

create table if not exists Executor (
    id serial Primary key,
    name text,
    surname text,
    mailbox text,
    numberOfCompletedProjects int,
    numberOfWorkers int
);

create table if not exists Works (
    id serial Primary key,
    name text,
    price int,
    priority text,
    needDaysToCompleted int,
    status text
);

create table if not exists listMaterials (
    id_work int not null,
    id_material int not null
);

create table if not exists listWorks (
    id_project int not null,
    id_work int not null
);

create table if not exists Project (
    id serial Primary key,
    name text,
    createdDate text,
    deadline text,
    numberOfWorkers int,
    projectAddress text,
    id_customer int,
    id_executor int
);
