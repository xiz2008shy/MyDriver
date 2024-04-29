package com.tom;

import com.tom.config.MySetting;
import com.tom.oss.AliyunOss;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AliyunOssDemo {

    public static void main(String[] args) throws FileNotFoundException {
        MySetting.initSetting(MySetting.mockParam());
        AliyunOss aliyunOss = new AliyunOss();
        aliyunOss.uploadFile("te/demo.txt",new FileInputStream("C:\\Users\\TOMQI\\Desktop\\1.txt"));
    }
}
