CREATE TABLE `file_record_table`
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `file_name`         varchar(255) NOT NULL,
    `relative_location` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
    `last_modified`     datetime     NOT NULL DEFAULT '1993-04-16 00:00:00' COMMENT '本地文件的最后修改时间',
    `md5`               varchar(100) DEFAULT NULL COMMENT '文件md5',
    `remote_path`       varchar(255) DEFAULT NULL COMMENT 'oss文件路径',
    `record_type`       int             default 0                     not null comment '0-文件，1-目录',
    `size`              bigint unsigned default 0                   not null comment '文件大小',
    PRIMARY KEY (`id`),
    KEY `file_record_table_file_name_IDX` (`file_name`) USING BTREE,
    KEY `file_record_table_relative_location_IDX` (`relative_location`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb3