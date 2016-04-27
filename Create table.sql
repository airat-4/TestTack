create table learn_group(
    id bigint primary key,
    group_number integer not null,
    dept_name varchar(100) not null
);
create table student(
    id bigint primary key,
    name varchar(50) not null,
    last_name varchar(50) not null,
    patronymic varchar(50) not null,
    born_date date not null,
    group_id bigint,
    foreign key (group_id) references learn_group(id)
);
