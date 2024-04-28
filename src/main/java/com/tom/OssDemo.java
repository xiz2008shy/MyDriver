package com.tom;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.tom.config.MySetting;

import java.io.ByteArrayInputStream;

public class OssDemo {

    public static void main(String[] args) throws Exception {
        MySetting.initSetting(MySetting.mockParam());

    }
}
