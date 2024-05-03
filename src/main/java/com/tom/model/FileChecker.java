package com.tom.model;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import com.tom.config.MySetting;
import com.tom.entity.FileRecord;
import com.tom.utils.MD5Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文件本地和远端检查类
 */
@Slf4j
public class FileChecker {

    private static final File[] EMPTY_ARR = new File[0];

    private final Set<String> fileNameSet = new HashSet<>(64);
    private final List<FileRecord> remoteRecords;
    private Map<String,FileRecord> remoteRecordsMap;

    private final File[] localFiles;

    private Map<String,File> localFilesMap;

    private boolean useRemoteMap;
    private boolean useLocalMap;

    /**
     * 子目录集合
     */
    @Getter
    private final List<File> subDirs;
    /**
     * 本地缺失文件
     */
    @Getter
    private final List<FileRecord> localLoseList;
    /**
     * 远端缺失文件
     */
    @Getter
    private final List<File> remoteLoseList;

    public record LRM(File localFile,FileRecord fileRecord,String md5){
    }

    /**
     * 待推送更新文件
     */
    @Getter
    private final List<LRM> pushList;
    /**
     * 待拉取更新的本地文件
     */
    @Getter
    private final List<LRM> pullList;

    private final String basePath = MySetting.getConfig().getBasePath();

    public FileChecker(List<FileRecord> remoteRecords, File[] localFiles) {
        this.remoteRecords = remoteRecords == null ? Collections.emptyList() : remoteRecords;
        this.localFiles = localFiles == null ? EMPTY_ARR : localFiles;
        if (remoteRecords != null && remoteRecords.size() > 10){
            this.remoteRecordsMap = remoteRecords.stream().collect(Collectors.toMap(FileRecord::getFileName, Function.identity(),(_, last) -> last));
            fileNameSet.addAll(remoteRecordsMap.keySet());
            useRemoteMap = true;
        }else if (remoteRecords != null){
            fileNameSet.addAll(remoteRecords.stream().map(FileRecord::getFileName).toList());
        }

        if (localFiles != null && localFiles.length > 10){
            this.localFilesMap = Arrays.stream(localFiles).collect(Collectors.toMap(File::getName, Function.identity(),(_, last) -> last));
            fileNameSet.addAll(localFilesMap.keySet());
            this.useLocalMap = true;
        }else if(localFiles != null) {
            fileNameSet.addAll(Arrays.stream(localFiles).map(File::getName).toList());
        }
        int maxSize = Math.max(this.remoteRecords.size(), this.localFiles.length);
        this.subDirs = new ArrayList<>(maxSize);
        this.localLoseList = new ArrayList<>(maxSize);
        this.remoteLoseList = new ArrayList<>(maxSize);
        this.pushList = new ArrayList<>(maxSize);
        this.pullList = new ArrayList<>(maxSize);
        check();
    }


    /**
     * 1.检查出本地缺失文件放入 localLoseList（集合为本地文件缺失的远端记录） ，并对本地缺失的目录进行创建
     * 2.检查出远端缺失记录放入 remoteLoseList（集合为远端记录缺失对应的本地文件）
     * 3.检查出所有的子目录，放入subDirs
     * @return 返回FileRecord集合，需要从远端下载或补充相关文件
     */
    public void check(){
        if (useLocalMap && useRemoteMap) {
            for (String fileName : fileNameSet) {
                File file = localFilesMap.get(fileName);
                FileRecord record = remoteRecordsMap.get(fileName);
                if (file == null){
                    addLocalLoseListAndTryAddSubDirList(remoteRecordsMap.get(fileName));
                }else if(record == null){
                    remoteLoseList.add(localFilesMap.get(fileName));
                    tryAddSubDirToList(file);
                }else {
                    checkWeatherPullOrPush(file,record);
                }
            }
        }else if (useLocalMap){
            for (String fileName : fileNameSet) {
                File file = localFilesMap.get(fileName);
                FileRecord record =  remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst().orElse(null);
                if (file == null){
                    remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst()
                            .ifPresentOrElse(this::addLocalLoseListAndTryAddSubDirList,this::logError);
                }else if(record == null){
                    remoteLoseList.add(localFilesMap.get(fileName));
                    tryAddSubDirToList(file);
                }else {
                    checkWeatherPullOrPush(file,record);
                }
            }
        }else if (useRemoteMap){
            for (String fileName : fileNameSet) {
                File file = Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
                FileRecord record = remoteRecordsMap.get(fileName);
                if (file == null){
                    addLocalLoseListAndTryAddSubDirList(remoteRecordsMap.get(fileName));
                }else if (record == null){
                    Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().ifPresentOrElse(remoteLoseList::add,this::logError);
                    tryAddSubDirToList(file);
                }else {
                    checkWeatherPullOrPush(file,record);
                }
            }
        }else {
            for (String fileName : fileNameSet) {
                File file = Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
                FileRecord record =  remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst().orElse(null);
                if (file == null){
                    remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst()
                            .ifPresentOrElse(this::addLocalLoseListAndTryAddSubDirList,this::logError);
                }else if (record == null){
                    Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().ifPresentOrElse(remoteLoseList::add,this::logError);
                    tryAddSubDirToList(file);
                }else {
                    checkWeatherPullOrPush(file,record);
                }
            }
        }
    }

    /**
     * 本地和远端都存在时，比较文件的最后修改时间，添加到push集合或者pull集合中，子目录则添加至subDir，外部需要对pushList、pullList、subDir进行处理
     * @param file
     * @param record
     */
    private void checkWeatherPullOrPush(File file, FileRecord record) {
        if (file.isDirectory()){
            this.subDirs.add(file);
        }else {
            long localModified = file.lastModified();
            long remoteModified = record.getLastModified().getTime();
            String localMd5 = MD5Util.getFileMD5(file);
            if ( localModified == remoteModified){
                if(file.length() != record.getSize()){
                    this.pushList.add(new LRM(file,record,localMd5));
                }else {
                    if (!localMd5.equals(record.getMd5())){
                        this.pushList.add(new LRM(file,record,localMd5));
                    }
                }
            }else if(localModified > remoteModified){
                if (!localMd5.equals(record.getMd5())){
                    this.pushList.add(new LRM(file,record,localMd5));
                }
            }else {
                this.pullList.add(new LRM(file,record,localMd5));
            }
        }
    }

    private void addLocalLoseListAndTryAddSubDirList(FileRecord fileRecord) {
        localLoseList.add(fileRecord);
        tryAddSubDirToList(fileRecord);
    }

    private void tryAddSubDirToList(FileRecord fileRecord) {
        if (fileRecord.getRecordType() == 1) {
            File mkDir = new File(basePath + fileRecord.getRelativeLocation() + fileRecord.getFileName());
            if (mkDir.mkdir()){
                subDirs.add(mkDir);
            }
        }
    }

    private void tryAddSubDirToList(File file) {
        if (file.isDirectory()) {
            subDirs.add(file);
        }
    }
    
    private void logError(){
        log.error("there is some error around here!");
    }
}
