package com.tom;

import com.tom.config.MySetting;
import com.tom.oss.AliyunOss;
import com.tom.utils.FileNameUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OssDownload {

    public static void main(String[] args) throws IOException {
        MySetting.initSetting(MySetting.mockParam());

        AliyunOss aliyunOss = new AliyunOss();
        String path = "C:\\Users\\TOMQI\\Desktop\\GAME\\Enscape_2023-07-14-19-10-22.png";
        FileChannel fileChannelOverFile = FileNameUtil.createFileChannelOverFile(path);
        aliyunOss.downloadFile("2024/5/16/01a14c473ba6414dafcc08393b93f755", fileChannelOverFile);

    }
}
