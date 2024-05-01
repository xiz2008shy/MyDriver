package com.tom.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.entity.FileRecord;
import com.tom.mapper.FileRecordMapper;
import com.tom.model.FileChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanJob {

    public static void main(String[] args) {
        MySetting.initSetting(MySetting.mockParam());
        ScanJob scanJob = new ScanJob();
        scanJob.scanBasePath();
    }


    public void scanBasePath(){
        String basePath = MySetting.getConfig().getBasePath();
        while (true){
            List<File> files = scanDir(List.of(new File(basePath)), basePath);
            if (CollUtil.isEmpty(files)){
                break;
            }
        }
    }

    /**
     * 广度优先便利
     * @param dirs 平级的目录
     * @param basePath 基础绝对路径
     */
    public List<File> scanDir(List<File> dirs,String basePath){
        List<File> subDir = new ArrayList<>();
        for (File dir : dirs) {
            File[] files = dir.listFiles();
            String relativePath = getRelativePath(dir, basePath);
            FileRecordMapper fileRecordMapper = MySetting.getRemoteMapper(FileRecordMapper.class);
            List<FileRecord> curPathRecords = fileRecordMapper.selectListByRelativeLocation(relativePath);
            FileChecker fileChecker = new FileChecker(curPathRecords, files);
            handleLocalLoseFile(fileChecker.checkLocalLose());
            handleRemoteLoseFile(fileChecker.checkRemoteLose());
            subDir.addAll(fileChecker.getSubDirs());
        }
        return subDir;
    }

    private static String getRelativePath(File dir, String basePath) {
        return StrUtil.blankToDefault(dir.getAbsolutePath().replace(basePath, StrUtil.EMPTY),"/");
    }


    /**
     * 处理本地缺失文件，要从oss下载文件到本地
     * @param loseLocalFile
     */
    private void handleLocalLoseFile(List<FileRecord> loseLocalFile){
        if (CollUtil.isEmpty(loseLocalFile)){
            return;
        }
        for (FileRecord fileRecord : loseLocalFile) {
            // TODO
        }
    }

    /**
     * 处理远端缺失记录，上传本地文件到oss，并添加远端记录
     * @param files
     */
    private void handleRemoteLoseFile(List<File> files){
        if (CollUtil.isEmpty(files)){
            return;
        }
        for (File file : files) {
            // TODO
        }
    }
}
