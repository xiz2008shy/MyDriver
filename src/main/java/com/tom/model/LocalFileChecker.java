package com.tom.model;

import com.tom.config.MySetting;
import com.tom.entity.LocalFileRecord;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class LocalFileChecker {


    private static final File[] EMPTY_ARR = new File[0];

    private final Set<String> fileNameSet = new HashSet<>(64);
    private final List<LocalFileRecord> localRecords;
    private Map<String,LocalFileRecord> localRecordsMap;

    private final File[] localFiles;

    private Map<String,File> localFilesMap;

    private boolean useLocalRecordsMap;
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
    private final List<LocalFileRecord> removeList;
    /**
     * 远端缺失文件
     */
    @Getter
    private final List<File> addList;

    public record LRM(File localFile,LocalFileRecord fileRecord,String md5){
    }


    private final String basePath = MySetting.getConfig().getBasePath();

    public LocalFileChecker(List<LocalFileRecord> localRecords, File[] localFiles) {
        this.localRecords = localRecords == null ? Collections.emptyList() : localRecords;
        this.localFiles = localFiles == null ? EMPTY_ARR : localFiles;
        if (localRecords != null && localRecords.size() > 10){
            this.localRecordsMap = localRecords.stream().collect(Collectors.toMap(LocalFileRecord::getFileName, Function.identity(),(_, last) -> last));
            fileNameSet.addAll(localRecordsMap.keySet());
            useLocalRecordsMap = true;
        }else if (localRecords != null){
            fileNameSet.addAll(localRecords.stream().map(LocalFileRecord::getFileName).toList());
        }

        if (localFiles != null && localFiles.length > 10){
            this.localFilesMap = Arrays.stream(localFiles).collect(Collectors.toMap(File::getName, Function.identity(),(_, last) -> last));
            fileNameSet.addAll(localFilesMap.keySet());
            this.useLocalMap = true;
        }else if(localFiles != null) {
            fileNameSet.addAll(Arrays.stream(localFiles).map(File::getName).toList());
        }
        int maxSize = Math.max(this.localRecords.size(), this.localFiles.length);
        this.subDirs = new ArrayList<>(maxSize);
        this.removeList = new ArrayList<>(maxSize);
        this.addList = new ArrayList<>(maxSize);
        check();
    }


    /**
     * 1.检查出本地缺失文件放入 localLoseList（集合为本地文件缺失的远端记录） ，并对本地缺失的目录进行创建
     * 2.检查出远端缺失记录放入 remoteLoseList（集合为远端记录缺失对应的本地文件）
     * 3.检查出所有的子目录，放入subDirs
     * @return 返回FileRecord集合，需要从远端下载或补充相关文件
     */
    public void check(){
        if (useLocalMap && useLocalRecordsMap) {
            for (String fileName : fileNameSet) {
                File file = localFilesMap.get(fileName);
                LocalFileRecord record = localRecordsMap.get(fileName);
                handleAndCheck(file, record);
            }
        }else if (useLocalMap){
            for (String fileName : fileNameSet) {
                File file = localFilesMap.get(fileName);
                LocalFileRecord record =  localRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst().orElse(null);
                handleAndCheck(file, record);
            }
        }else if (useLocalRecordsMap){
            for (String fileName : fileNameSet) {
                File file = Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
                LocalFileRecord record = localRecordsMap.get(fileName);
                handleAndCheck(file, record);
            }
        }else {
            for (String fileName : fileNameSet) {
                File file = Arrays.stream(localFiles).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
                LocalFileRecord record =  localRecords.stream().filter(f -> f.getFileName().equals(fileName)).findFirst().orElse(null);
                handleAndCheck(file, record);
            }
        }
    }

    private void handleAndCheck(File file, LocalFileRecord record) {
        if (file == null){
            removeList.add(record);
            tryAddSubDirToList(record);
        }else if(record == null){
            addList.add(file);
            tryAddSubDirToList(file);
        }
    }


    private void tryAddSubDirToList(LocalFileRecord fileRecord) {
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
}
