package com.tom.model;

import com.tom.config.MySetting;
import com.tom.entity.FileRecord;
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

    @Getter
    private final List<File> subDirs;
    private final List<FileRecord> localLoseList;

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
    }


    /**
     * 检查出本地缺失文件，并对本地缺失的目录进行创建，以及将所有次级目录添加进subDirList
     * @return 返回FileRecord集合，需要从远端下载或补充相关文件
     */
    public List<FileRecord> checkLocalLose(){
        if (useLocalMap && useRemoteMap) {
            for (String fileName : fileNameSet) {
                File file = localFilesMap.get(fileName);
                if (file == null){
                    addLocalLoseListAndTryAddSubDirList(remoteRecordsMap.get(fileName));
                }else {
                    tryAddSubDirToList(file);
                }
            }
        }else if (useLocalMap){
            for (String fileName : fileNameSet) {
                File file = localFilesMap.get(fileName);
                if (file == null){
                    remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst()
                            .ifPresentOrElse(this::addLocalLoseListAndTryAddSubDirList,this::logError);
                }else {
                    tryAddSubDirToList(file);
                }
            }
        }else if (useRemoteMap){
            for (String fileName : fileNameSet) {
                File file = Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
                if (file == null){
                    addLocalLoseListAndTryAddSubDirList(remoteRecordsMap.get(fileName));
                }else {
                    tryAddSubDirToList(file);
                }
            }
        }else {
            for (String fileName : fileNameSet) {
                File file = Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
                if (file == null){
                    remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst()
                            .ifPresentOrElse(this::addLocalLoseListAndTryAddSubDirList,this::logError);
                }else {
                    tryAddSubDirToList(file);
                }
            }
        }
        return localLoseList;
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


    /**
     * 获取远端记录缺失的文件
     * @return 返回File集合，意味这上传这些文件以及增加远端记录
     */
    public List<File> checkRemoteLose(){
        List<File> remoteLoseList = new ArrayList<>();
        if (useLocalMap && useRemoteMap) {
            for (String fileName : fileNameSet) {
                FileRecord record = remoteRecordsMap.get(fileName);
                if (record == null){
                    remoteLoseList.add(localFilesMap.get(fileName));
                }
            }
        }else if (useLocalMap){
            for (String fileName : fileNameSet) {
                FileRecord record =  remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst().orElse(null);
                if (record == null){
                    remoteLoseList.add(localFilesMap.get(fileName));
                }
            }
        }else if (useRemoteMap){
            for (String fileName : fileNameSet) {
                FileRecord record = remoteRecordsMap.get(fileName);
                if (record == null){
                    Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().ifPresentOrElse(remoteLoseList::add,this::logError);
                }
            }
        }else {
            for (String fileName : fileNameSet) {
                FileRecord record =  remoteRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst().orElse(null);
                if (record == null){
                    Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().ifPresentOrElse(remoteLoseList::add,this::logError);
                }
            }
        }
        return remoteLoseList;
    }
    
    
    private void logError(){
        log.error("there is some error around here!");
    }
}
