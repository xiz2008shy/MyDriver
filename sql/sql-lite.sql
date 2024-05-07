-- auto-generated definition
create table file_record_table
(
    id                INTEGER primary key autoincrement,
    file_name         VARCHAR(255) not null,
    relative_location VARCHAR(500) not null,
    last_modified     TEXT default '1993-04-16 00:00:00',
    md5               varchar(100) default '' not null,
    record_type       int default 0 not null,
    size              bigint unsigned default 0
);