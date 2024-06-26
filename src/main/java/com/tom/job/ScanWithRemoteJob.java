package com.tom.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.entity.FileRecord;
import com.tom.entity.LocalFileRecord;
import com.tom.entity.RemoteOperateHistory;
import com.tom.model.FileChecker;
import com.tom.oss.AliyunOss;
import com.tom.repo.LocalRecordService;
import com.tom.repo.RemoteFileRecordService;
import com.tom.repo.RemoteOperateHistoryService;
import com.tom.utils.FileNameUtil;
import com.tom.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.text.StrPool.DOT;

@Slf4j
public class ScanWithRemoteJob {

    private RemoteFileRecordService remoteFileRecordService = RemoteFileRecordService.getService();
    private LocalRecordService localRecordService = LocalRecordService.getService();
    private RemoteOperateHistoryService remoteOperateHistoryService = RemoteOperateHistoryService.getService();
    private AliyunOss aliyunOss = new AliyunOss();

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
            if (!dir.exists()){
                continue;
            }
            File[] files = dir.listFiles();
            String relativePath = FileNameUtil.getRelativePath(dir, basePath);

            List<FileRecord> curPathRecords = remoteFileRecordService.selectListByRelativeLocation(relativePath);
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
            // 远端更新超过本地文件内的场合
            List<FileChecker.LRM> pullList = fileChecker.getPullList();
            handlePullList(pullList);

            subDir.addAll(fileChecker.getSubDirs());
        }
        return subDir;
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
                try(FileChannel outChannel = FileNameUtil.createFileChannelCEW(curDir + File.separator + fileRecord.getFileName());) {
                    aliyunOss.downloadFile(fileRecord.getRemotePath(),outChannel);
                    LocalFileRecord localFileRecord = new LocalFileRecord();
                    localFileRecord.copyFrom(fileRecord);
                    localRecordService.insert(localFileRecord);
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
            // 增加远端删除的场景，需同步删除本地文件，1判断是否有远端删除记录
            String md5 = MD5Util.getFileMD5(file);
            try {
                RemoteOperateHistory deleteHistory = remoteOperateHistoryService.selectByMd5AndFilenameDel(md5, file.getName(),relativePath);
                if (deleteHistory == null) {
                    insertRecordAndUploadFile(relativePath, file, remotePath,MySetting.getMacIP(),md5);
                }else {
                    if (file.isDirectory()){
                        localRecordService.removeUponDir(relativePath + file.getName() + "/");
                    }
                    localRecordService.removeFile(relativePath, file.getName());
                    file.delete();
                }
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
            fileRecord.setLastModified(file.lastModified());
            fileRecord.setSize(0);
            if (!file.isDirectory()){
                try {
                    fileRecord.setRemotePath(remotePath + UUID.fastUUID().toString(true));
                    fileRecord.setSize(file.length());
                    fileRecord.setMd5(lrm.md5());
                    aliyunOss.uploadFile(fileRecord.getRemotePath(), lrm.localFile());
                    remoteFileRecordService.updateById(fileRecord);
                }catch (Exception e){
                    log.error("ScanJob.handleRemoteLoseFile occurred an error,cause:",e);
                }
            }else {
                remoteFileRecordService.updateById(fileRecord);
            }
            addPushOperateRecord(MySetting.getMacIP(), fileRecord);
        }
    }


    private void handlePullList(List<FileChecker.LRM> pullList){
        if (CollUtil.isEmpty(pullList)){
            return;
        }

        for (FileChecker.LRM lrm : pullList) {
            File file = lrm.localFile();
            FileRecord fileRecord = lrm.fileRecord();

            // 需要下载远端文件 并更新本地记录
            try (FileChannel outChannel = FileNameUtil.createFileChannelCEW(file.toPath());) {
                aliyunOss.downloadFile(fileRecord.getRemotePath(),outChannel);
                LocalFileRecord localFileRecord = new LocalFileRecord().copyFrom(fileRecord);
                localRecordService.updateFile(localFileRecord);
            }catch (Exception e){
                log.error("ScanJob.handlePullList occurred an error,cause:",e);
            }
        }
    }




    private void insertRecordAndUploadFile(String relativePath, File file, String remotePath,String macAddr,String md5) {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setFileName(file.getName());
        fileRecord.setLastModified(file.lastModified());
        fileRecord.setRelativeLocation(relativePath);
        if (file.isDirectory()){
            fileRecord.setSize(0);
            fileRecord.setRecordType(1);
            fileRecord.setMd5(StrUtil.EMPTY);
        }else {
            List<FileRecord> similarFile = remoteFileRecordService.selectListByMd5AndSize(md5, file.length());
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
        remoteFileRecordService.insert(fileRecord);

        // 远端库 添加push记录
        addPushOperateRecord(macAddr, fileRecord);
    }

    /**
     * 远端库 添加push记录
     * @param macAddr
     * @param fileRecord
     */
    private void addPushOperateRecord(String macAddr, FileRecord fileRecord) {
        RemoteOperateHistory remoteOperateHistory = new RemoteOperateHistory();
        remoteOperateHistory.copyFrom(fileRecord);
        remoteOperateHistory.setOperate("push");
        remoteOperateHistory.setOperator(macAddr);
        remoteOperateHistoryService.insert(remoteOperateHistory);
    }
}
