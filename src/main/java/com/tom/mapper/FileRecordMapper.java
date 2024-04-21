package com.tom.mapper;

import com.tom.entity.FileRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FileRecordMapper {

    List<FileRecord> selectList();

    @Select("select 1")
    String test();
}
