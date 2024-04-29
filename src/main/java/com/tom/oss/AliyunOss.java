package com.tom.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.tom.component.ProcessBar;
import com.tom.config.MySetting;
import com.tom.config.vo.ConfigVo;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class AliyunOss implements OssOperation{

    @Override
    public PutObjectResult uploadFile(String filePath, InputStream inputStream){
        /**
         * https://help.aliyun.com/zh/oss/user-guide/regions-and-endpoints?spm=a2c6h.13066369.question.4.11381fd1rrAmLg
         * 根据开通bucket的地域选择，填入不同的地址，不同区域对应的地址可在以上查看
         */
        ConfigVo config = MySetting.getConfig();
        String endpoint = STR."https://\{config.getOssEndpoint()}.aliyuncs.com";
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = config.getBucketName();
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        try {
            return ossClient.putObject(new PutObjectRequest(bucketName, filePath, inputStream)
                    .withProgressListener(new ProcessBar.UploadProgressListener()));
        } catch (OSSException oe) {
            log.error("OSSException occurred,Error Message:{},Code:{},Request ID:{},Host ID:{}",
                    oe.getErrorMessage(),oe.getErrorCode(),oe.getRequestId(), oe.getHostId());
        } catch (ClientException ce) {
            log.error("ClientException occurred,Error Message:{}", ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

}
