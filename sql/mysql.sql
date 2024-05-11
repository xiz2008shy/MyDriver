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
    deleted         tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否删除，默认为0，1表示暂时删除',
    operate_time    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `file_record_table_file_name_IDX` (`file_name`) USING BTREE,
    KEY `file_record_table_relative_location_IDX` (`relative_location`) USING BTREE,
    KEY `file_record_table_md5_IDX` (`md5`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE `file_operate_history`
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    file_name         varchar(255)                                  not null,
    relative_location varchar(255)    default '/'                   not null,
    last_modified     datetime        default '1993-04-16 00:00:00' not null comment '本地文件的最后修改时间',
    md5               varchar(100)    default ''                    not null comment '文件md5',
    remote_path       varchar(255)    default ''                    not null comment 'oss文件路径',
    record_type       int             default 0                     not null comment '0-文件，1-目录',
    size              bigint unsigned default 0                     not null comment '文件大小',
    operate_time      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
    operate           varchar(10)     default 'push'                not null comment '远端文件操作，push推送，delete删除',
    operator          varchar(64)     default ''                    not null comment '操作设备',
    PRIMARY KEY (`id`),
    KEY `file_name_IDX` (`file_name`) USING BTREE,
    KEY `md5_IDX` (`md5`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4;


