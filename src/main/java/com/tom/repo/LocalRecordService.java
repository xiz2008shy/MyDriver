package com.tom.repo;

import com.tom.entity.LocalFileRecord;
import com.tom.mapper.LocalRecordMapper;
import com.tom.utils.JDBCUtil;
import lombok.Getter;

import java.util.List;

public class LocalRecordService implements LocalRecordMapper {
    @Getter
    private final static LocalRecordService service = new LocalRecordService();

    private LocalRecordService() {
    }

    @Override
    public void createTable() {
        LocalRecordMapper localMapper = JDBCUtil.getLocalMapper(LocalRecordMapper.class);
        localMapper.createTable();
    }

    @Override
    public List<LocalFileRecord> selectListByRelativeLocation(String relativePath) {
        LocalRecordMapper localMapper = JDBCUtil.getLocalMapper(LocalRecordMapper.class);
        return localMapper.selectListByRelativeLocation(relativePath);
    }

    @Override
    public List<LocalFileRecord> selectListByRelativeLocationUpon(String relativePath) {
        LocalRecordMapper localMapper = JDBCUtil.getLocalMapper(LocalRecordMapper.class);
        return localMapper.selectListByRelativeLocationUpon(relativePath);
    }

    @Override
    public void insert(LocalFileRecord fileRecord) {
        LocalRecordMapper localMapper = JDBCUtil.getLocalMapper(LocalRecordMapper.class);
        localMapper.insert(fileRecord);
    }

    @Override
    public void saveBatch(List<LocalFileRecord> fileRecords) {
        LocalRecordMapper localMapper = JDBCUtil.getLocalMapper(LocalRecordMapper.class);
        localMapper.saveBatch(fileRecords);
    }

    @Override
    public void removeBatch(List<LocalFileRecord> fileRecords) {
        LocalRecordMapper localMapper = JDBCUtil.getLocalMapper(LocalRecordMapper.class);
        localMapper.removeBatch(fileRecords);
    }
}
