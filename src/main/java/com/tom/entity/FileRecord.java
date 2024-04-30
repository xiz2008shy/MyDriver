package com.tom.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FileRecord implements Serializable {

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
     * 本地的最后修改时间
     */
    private Date lastModified;

    /**
     * 文件的md5码值
     */
    private String md5;

    /**
     * oss上的保存路径
     */
    private String remotePath;

}
