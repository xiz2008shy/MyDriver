package com.tom.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.entity.LocalFileRecord;
import com.tom.model.LocalFileChecker;
import com.tom.repo.LocalRecordService;
import com.tom.repo.RemoteFileRecordService;
import com.tom.utils.MD5Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanWithLocalJob {

    private final LocalRecordService localRecordService = LocalRecordService.getService();
    private final RemoteFileRecordService remoteFileRecordService = RemoteFileRecordService.getService();
    private final List<LocalFileRecord> removeList = new ArrayList<>();
    private final List<LocalFileRecord> addList = new ArrayList<>();

    public void scanBasePathCompareWithLocal(){
        String basePath = MySetting.getConfig().getBasePath();
        List<File> files = List.of(new File(basePath));
        do {
            files = scanDirCompareWithLocal(files, basePath);
        } while (!CollUtil.isEmpty(files));

        if (CollUtil.isNotEmpty(addList)){
            localRecordService.saveBatch(addList);
        }

        if (CollUtil.isNotEmpty(removeList)){
            List<LocalFileRecord> dirRecords = new ArrayList<>();
            for (LocalFileRecord record : removeList) {
                if (record.getRecordType()==1){
                    List<LocalFileRecord> dirR = localRecordService.selectListByRelativeLocationUpon(
                            record.getRelativeLocation() + record.getFileName() + "/");
                    dirRecords.addAll(dirR);
                }
            }
            removeList.addAll(dirRecords);
            localRecordService.removeBatch(removeList);
            remoteFileRecordService.tempRemoveBatch(removeList);
        }
    }

    /**
     * 广度优先便利
     * @param dirs 平级的目录
     * @param basePath 基础绝对路径
     */
    public List<File> scanDirCompareWithLocal(List<File> dirs,String basePath){
        List<File> subDir = new ArrayList<>();

        for (File dir : dirs) {
            File[] files = dir.listFiles();
            String relativePath = getRelativePath(dir, basePath);
            List<LocalFileRecord> curPathRecords = localRecordService.selectListByRelativeLocation(relativePath);
            LocalFileChecker fileChecker = new LocalFileChecker(curPathRecords, files);
            removeList.addAll(fileChecker.getRemoveList());
            for (File file : fileChecker.getAddList()) {
                LocalFileRecord localFileRecord = new LocalFileRecord();
                localFileRecord.setFileName(file.getName());
                localFileRecord.setRecordType(file.isDirectory() ? 1:0);
                localFileRecord.setLastModified(String.valueOf(file.lastModified()));
                localFileRecord.setRelativeLocation(relativePath);
                if (!file.isDirectory()) {
                    String fileMD5 = MD5Util.getFileMD5(file);
                    localFileRecord.setMd5(fileMD5);
                    localFileRecord.setSize(file.length());
                }else {
                    localFileRecord.setSize(0);
                    localFileRecord.setMd5(StrUtil.EMPTY);
                }

                addList.add(localFileRecord);
            }

            subDir.addAll(fileChecker.getSubDirs());
        }
        return subDir;
    }

    /**
     * 获取相对于基础路径的相对路径
     * @param dir
     * @param basePath
     * @return
     */
    private static String getRelativePath(File dir, String basePath) {
        return STR."\{dir.getAbsolutePath().replace(basePath, StrUtil.EMPTY).replaceAll("\\\\", "/")}/";
    }

}
