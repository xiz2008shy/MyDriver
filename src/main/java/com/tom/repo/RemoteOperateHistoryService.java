package com.tom.repo;

import com.tom.entity.RemoteOperateHistory;
import com.tom.mapper.RemoteOperateHistoryMapper;
import com.tom.utils.JDBCUtil;
import lombok.Getter;

import java.util.List;

public class RemoteOperateHistoryService implements RemoteOperateHistoryMapper {
    @Getter
    private static final RemoteOperateHistoryService service = new RemoteOperateHistoryService();

    private RemoteOperateHistoryService() {
    }

    @Override
    public void insert(RemoteOperateHistory fileRecord) {
        RemoteOperateHistoryMapper remoteMapper = JDBCUtil.getRemoteMapper(RemoteOperateHistoryMapper.class);
        remoteMapper.insert(fileRecord);
    }

    @Override
    public void saveBatch(List<RemoteOperateHistory> fileRecords) {
        RemoteOperateHistoryMapper remoteMapper = JDBCUtil.getRemoteMapper(RemoteOperateHistoryMapper.class);
        remoteMapper.saveBatch(fileRecords);
    }

    @Override
    public RemoteOperateHistory selectByMd5AndFilenameDel(String md5, String filename,String relativePath) {
        RemoteOperateHistoryMapper remoteMapper = JDBCUtil.getRemoteMapper(RemoteOperateHistoryMapper.class);
        return remoteMapper.selectByMd5AndFilenameDel(md5,filename,relativePath);
    }
}
