package com.tom.entity;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LocalFileRecord implements Serializable {

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
    private String lastModified;

    /**
     * 文件类型 0 文件 1目录
     */
    private int recordType;

    private long size;

    public LocalFileRecord copyFrom(FileRecord fileRecord) {
        BeanUtil.copyProperties(fileRecord,this,false);
        this.lastModified = String.valueOf(fileRecord.getLastModified());
        return this;
    }
}
