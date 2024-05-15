package com.tom;

import com.tom.config.MySetting;
import com.tom.entity.FileRecord;
import com.tom.mapper.FileRecordMapper;
import com.tom.utils.JDBCUtil;

import java.util.List;

public class Sample
{
    public static void main(String[] args) throws Exception {
        MySetting.initSetting(MySetting.mockParam());
        JDBCUtil.createStableConnection();
        FileRecordMapper recordMapper = JDBCUtil.getRemoteMapper(FileRecordMapper.class);
        List<FileRecord> fileRecords = recordMapper.selectListByRelativeLocation("xx");
        System.out.println(fileRecords);
    }
}