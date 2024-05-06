package com.tom.entity;

import java.util.Date;

public class LocalFileRecord {

    private long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 本地相对路径（父目录的相对路径）
     */
    private String relativeLocation;

    /**
     * 文件的md5码值
     */
    private String md5;

    /**
     * 最后修改时间
     */
    private Date lastModified;

    /**
     * 文件类型 0 文件 1目录
     */
    private int recordType;
}
