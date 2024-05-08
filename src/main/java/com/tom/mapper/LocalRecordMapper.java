package com.tom.mapper;

import com.tom.entity.LocalFileRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocalRecordMapper {

    void createTable();

    List<LocalFileRecord> selectListByRelativeLocation(@Param("relativePath") String relativePath);

    void insert(LocalFileRecord fileRecord);

    void saveBatch(@Param("fileRecords") List<LocalFileRecord> fileRecords);

    void removeBatch(@Param("fileRecords") List<LocalFileRecord> fileRecords);
}
