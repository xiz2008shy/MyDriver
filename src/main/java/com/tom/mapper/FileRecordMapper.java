package com.tom.mapper;

import com.tom.entity.FileRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileRecordMapper {

    List<FileRecord> selectListByRelativeLocation(@Param("relativePath") String relativePath);

    void insert(FileRecord fileRecord);

    int updateById(FileRecord fileRecord);

}
