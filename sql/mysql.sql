CREATE TABLE `file_record_table`
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    file_name         varchar(255)                                  not null,
    relative_location varchar(255)    default '/'                   not null,
    last_modified     datetime        default '1993-04-16 00:00:00' not null comment '本地文件的最后修改时间',
    md5               varchar(100)    default ''                    not null comment '文件md5',
    remote_path       varchar(255)    default ''                    not null comment 'oss文件路径',
    record_type       int             default 0                     not null comment '0-文件，1-目录',
    size              bigint unsigned default 0                     not null comment '文件大小',
    PRIMARY KEY (`id`),
    KEY `file_record_table_file_name_IDX` (`file_name`) USING BTREE,
    KEY `file_record_table_relative_location_IDX` (`relative_location`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;