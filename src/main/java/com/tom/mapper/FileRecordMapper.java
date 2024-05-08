package com.tom.mapper;

import com.tom.entity.FileRecord;
import com.tom.entity.LocalFileRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileRecordMapper {

    List<FileRecord> selectListByRelativeLocation(@Param("relativePath") String relativePath);

    void insert(FileRecord fileRecord);

    int updateById(FileRecord fileRecord);

    List<FileRecord> selectListByMd5AndSize(@Param("md5") String md5, @Param("size") long size);


    void tempRemoveBatch(@Param("fileRecords") List<LocalFileRecord> fileRecords);

}
