package com.tom;

import com.tom.config.MySetting;
import com.tom.oss.AliyunOss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class AliyunOssDemo {

    public static void main(String[] args) throws FileNotFoundException {
        MySetting.initSetting(MySetting.mockParam());
        AliyunOss aliyunOss = new AliyunOss();
        aliyunOss.uploadFile("te/demo.txt",new File("C:\\Users\\TOMQI\\Desktop\\1.txt"));
        aliyunOss.downloadFile("te/demo.txt",new FileOutputStream("C:\\Users\\TOMQI\\Desktop\\demo.txt"));
    }
}
