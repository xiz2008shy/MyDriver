package com.tom.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class RemoteOperateHistory implements Serializable {

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
    /**
     * 文件大小（目录为0）
     */
    private long size;
    /**
     * 文件类型 0 文件 1目录
     */
    private int recordType;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作(push,deleted)
     */
    private String operate;
    /**
     * 操作者（记mac地址）
     */
    private String operator;

    public void copyFrom(FileRecord record){
        BeanUtil.copyProperties(record,this,true);
        this.lastModified = DateUtil.date(record.getLastModified());
    }
}
