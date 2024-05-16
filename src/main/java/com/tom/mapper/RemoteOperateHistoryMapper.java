package com.tom.mapper;

import com.tom.entity.RemoteOperateHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RemoteOperateHistoryMapper {

    void createTableIfNotExist();

    void insert(RemoteOperateHistory fileRecord);

    void saveBatch(@Param("fileRecords")List<RemoteOperateHistory> fileRecords);

    RemoteOperateHistory selectByMd5AndFilenameDel(@Param("md5") String md5, @Param("filename") String filename, @Param("relativePath") String relativePath);
}
