package com.tom.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
public class MyDriverOss {

    public void uploadFile(String filePath, InputStream inputStream){
        /**
         * https://help.aliyun.com/zh/oss/user-guide/regions-and-endpoints?spm=a2c6h.13066369.question.4.11381fd1rrAmLg
         * 根据开通bucket的地域选择，填入不同的地址，不同区域对应的地址可在以上查看
         */
        String endpoint = "https://oss-cn-zhangjiakou.aliyuncs.com";
        String accessKeyId = "---";
        String accessKeySecret = "---";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "mydriver123";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        try {
            ossClient.putObject(bucketName, filePath, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
