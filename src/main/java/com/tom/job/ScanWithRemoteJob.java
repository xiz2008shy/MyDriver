package com.tom.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.entity.FileRecord;
import com.tom.mapper.FileRecordMapper;
import com.tom.model.FileChecker;
import com.tom.oss.AliyunOss;
import com.tom.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.text.StrPool.DOT;

@Slf4j
public class ScanWithRemoteJob {
    AliyunOss aliyunOss = new AliyunOss();

    public void scanBasePathCompareWithRemote(){
        String basePath = MySetting.getConfig().getBasePath();
        List<File> files = List.of(new File(basePath));
        do {
            files = scanDirCompareWithRemote(files, basePath);
        } while (!CollUtil.isEmpty(files));
    }

    /**
     * 广度优先便利
     * @param dirs 平级的目录
     * @param basePath 基础绝对路径
     */
    public List<File> scanDirCompareWithRemote(List<File> dirs,String basePath){
        List<File> subDir = new ArrayList<>();
        for (File dir : dirs) {
            File[] files = dir.listFiles();
            String relativePath = getRelativePath(dir, basePath);
            FileRecordMapper fileRecordMapper = MySetting.getRemoteMapper(FileRecordMapper.class);
            List<FileRecord> curPathRecords = fileRecordMapper.selectListByRelativeLocation(relativePath);
            FileChecker fileChecker = new FileChecker(curPathRecords, files);

            // 处理远端有记录，但本地文件不存在的记录
            List<FileRecord> localLoseList = fileChecker.getLocalLoseList();
            handleLocalLoseFile(localLoseList,dir.getAbsolutePath());

            // 处理远端不存在记录，但本地存的文件
            List<File> remoteLoseList = fileChecker.getRemoteLoseList();
            handleRemoteLoseFile(remoteLoseList,relativePath);

            // 处理本地文件在远端记录后有更新的文件
            List<FileChecker.LRM> pushList = fileChecker.getPushList();
            handlePushList(pushList);
            List<FileChecker.LRM> pullList = fileChecker.getPullList();
            // 远端更新超过本地文件内的场合

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


    /**
     * 处理本地缺失文件，要从oss下载文件到本地
     * @param loseLocalFile 本地缺失文件记录
     */
    private void handleLocalLoseFile(List<FileRecord> loseLocalFile,String curDir){
        if (CollUtil.isEmpty(loseLocalFile)){
            return;
        }
        for (FileRecord fileRecord : loseLocalFile) {
            if (fileRecord.getRecordType() == 0){
                try(var fileOutputStream = new FileOutputStream(curDir + File.separator + fileRecord.getFileName())) {
                    aliyunOss.downloadFile(fileRecord.getRemotePath(),fileOutputStream);
                } catch (Exception e) {
                    log.error("ScanJob.handleLocalLoseFile occurred an error,cause:",e);
                }
            }
        }
    }

    /**
     * 处理远端缺失记录，上传本地文件到oss，并添加远端记录
     * @param files 远端缺失文件
     */
    private void handleRemoteLoseFile(List<File> files,String relativePath){
        if (CollUtil.isEmpty(files)){
            return;
        }
        DateTime date = DateUtil.date();
        String remotePath = STR."\{date.year()}/\{date.monthBaseOne()}/\{date.dayOfMonth()}/";

        for (File file : files) {
            try {
                insertRecordAndUploadFile(relativePath, file, remotePath);
            }catch (Exception e){
                log.error("ScanJob.handleRemoteLoseFile occurred an error,cause:",e);
            }
        }
    }


    private void handlePushList(List<FileChecker.LRM> pushList){
        if (CollUtil.isEmpty(pushList)){
            return;
        }
        DateTime date = DateUtil.date();
        String remotePath = STR."\{date.year()}/\{date.monthBaseOne()}/\{date.dayOfMonth()}/";

        for (FileChecker.LRM lrm : pushList) {
            File file = lrm.localFile();
            FileRecord fileRecord = lrm.fileRecord();
            fileRecord.setLastModified(DateUtil.date(file.lastModified()));
            fileRecord.setSize(file.length());
            if (!file.isDirectory()){
                try {
                    fileRecord.setRemotePath(remotePath + UUID.fastUUID().toString(true));
                    fileRecord.setLastModified(DateUtil.date(file.lastModified()));
                    fileRecord.setSize(file.length());
                    fileRecord.setMd5(lrm.md5());
                    aliyunOss.uploadFile(fileRecord.getRemotePath(), lrm.localFile());
                }catch (Exception e){
                    log.error("ScanJob.handleRemoteLoseFile occurred an error,cause:",e);
                }
            }
            FileRecordMapper fileRecordMapper = MySetting.getRemoteMapper(FileRecordMapper.class);
            fileRecordMapper.updateById(fileRecord);
        }
    }


    private void insertRecordAndUploadFile(String relativePath, File file, String remotePath) {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setFileName(file.getName());
        fileRecord.setLastModified(DateUtil.date(file.lastModified()));
        fileRecord.setRelativeLocation(relativePath);
        if (file.isDirectory()){
            fileRecord.setSize(0);
            fileRecord.setRecordType(1);
            fileRecord.setMd5(StrUtil.EMPTY);
        }else {
            FileRecordMapper remoteMapper = MySetting.getRemoteMapper(FileRecordMapper.class);
            String md5 = MD5Util.getFileMD5(file);
            List<FileRecord> similarFile = remoteMapper.selectListByMd5AndSize(md5, file.length());
            if (CollUtil.isEmpty(similarFile)){
                fileRecord.setRemotePath(remotePath + UUID.fastUUID().toString(true));
                aliyunOss.uploadFile(fileRecord.getRemotePath(), file);
            }else {
                String name = file.getName();
                String fileExtendType = name.substring(name.lastIndexOf(DOT));
                boolean findSameFile = false;
                for (FileRecord record : similarFile) {
                    String recordExtendType = record.getFileName().substring(record.getFileName().lastIndexOf(DOT));
                    if (recordExtendType.equals(fileExtendType)){
                        fileRecord.setRemotePath(record.getRemotePath());
                        findSameFile = true;
                        break;
                    }
                }
                if (!findSameFile){
                    fileRecord.setRemotePath(remotePath + UUID.fastUUID().toString(true));
                    aliyunOss.uploadFile(fileRecord.getRemotePath(), file);
                }
            }

            fileRecord.setSize(file.length());
            fileRecord.setRecordType(0);
            fileRecord.setMd5(md5);
        }
        FileRecordMapper fileRecordMapper = MySetting.getRemoteMapper(FileRecordMapper.class);
        fileRecordMapper.insert(fileRecord);
    }
}
