package com.tom.job;

import cn.hutool.core.collection.CollUtil;
import com.tom.config.MySetting;

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
    }

    /**
     * 广度优先便利
     * @param dir
     */
    public void scanDir(File dir){
        File[] files = dir.listFiles();
        List<File> subDir = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    subDir.add(file);
                }else {
                    // TODO scan
                }
            }
            if (CollUtil.isNotEmpty(subDir)){
                for (File file : subDir) {
                    scanDir(file);
                }
            }
        }
    }


    public void scanFile(File file){

    }
}
