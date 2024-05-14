package com.tom.repo;

import com.tom.entity.FileRecord;
import com.tom.entity.LocalFileRecord;
import com.tom.mapper.FileRecordMapper;
import com.tom.utils.JDBCUtil;
import lombok.Getter;

import java.util.List;

public class RemoteFileRecordService implements FileRecordMapper{

    @Getter
    private static final RemoteFileRecordService service = new RemoteFileRecordService();

    private RemoteFileRecordService() {
    }

    @Override
    public List<FileRecord> selectListByRelativeLocation(String relativePath) {
        FileRecordMapper remoteMapper = JDBCUtil.getRemoteMapper(FileRecordMapper.class);
        return remoteMapper.selectListByRelativeLocation(relativePath);
    }

    @Override
    public void insert(FileRecord fileRecord) {
        FileRecordMapper remoteMapper = JDBCUtil.getRemoteMapper(FileRecordMapper.class);
        remoteMapper.insert(fileRecord);
    }

    @Override
    public int updateById(FileRecord fileRecord) {
        FileRecordMapper remoteMapper = JDBCUtil.getRemoteMapper(FileRecordMapper.class);
        return remoteMapper.updateById(fileRecord);
    }

    @Override
    public List<FileRecord> selectListByMd5AndSize(String md5, long size) {
        FileRecordMapper remoteMapper = JDBCUtil.getRemoteMapper(FileRecordMapper.class);
        return remoteMapper.selectListByMd5AndSize(md5,size);
    }

    @Override
    public void tempRemoveBatch(List<LocalFileRecord> fileRecords) {
        FileRecordMapper remoteMapper = JDBCUtil.getRemoteMapper(FileRecordMapper.class);
        remoteMapper.tempRemoveBatch(fileRecords);
    }
}
