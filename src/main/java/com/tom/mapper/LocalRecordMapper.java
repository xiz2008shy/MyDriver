package com.tom.mapper;

import com.tom.entity.LocalFileRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocalRecordMapper {

    void createTable();

    LocalFileRecord selectByRlAndFn(@Param("relativePath") String relativePath,@Param("filename") String filename);

    List<LocalFileRecord> selectListByRelativeLocation(@Param("relativePath") String relativePath);

    List<LocalFileRecord> selectListByRelativeLocationUpon(@Param("relativePath") String relativePath);

    void insert(LocalFileRecord fileRecord);

    void saveBatch(@Param("fileRecords") List<LocalFileRecord> fileRecords);

    void removeBatch(@Param("fileRecords") List<LocalFileRecord> fileRecords);

    void removeUponDir(@Param("relativePath") String relativePath);

    void removeFile(@Param("relativePath") String relativePath,@Param("filename") String filename);

}
