package com.tom.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.tom.component.ProcessBar;
import com.tom.config.MySetting;
import com.tom.config.vo.ConfigVo;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.*;

@Slf4j
public class AliyunOss implements OssOperation{

    private static OSS ossClient = createOssClient();

    public static OSS createOssClient(){
        /**
         * https://help.aliyun.com/zh/oss/user-guide/regions-and-endpoints?spm=a2c6h.13066369.question.4.11381fd1rrAmLg
         * 根据开通bucket的地域选择，填入不同的地址，不同区域对应的地址可在以上查看
         */
        ConfigVo config = MySetting.getConfig();
        String endpoint = STR."https://\{config.getOssEndpoint()}.aliyuncs.com";
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);
    }

    @Override
    public PutObjectResult uploadFile(String filePath, File toUpload){
        ConfigVo config = MySetting.getConfig();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = config.getBucketName();
        try {
            return ossClient.putObject(new PutObjectRequest(bucketName, filePath, toUpload)
                    .withProgressListener(new ProcessBar.UploadProgressListener(toUpload)));
        } catch (OSSException oe) {
            log.error("OSSException occurred,Error Message:{},Code:{},Request ID:{},Host ID:{}",
                    oe.getErrorMessage(),oe.getErrorCode(),oe.getRequestId(), oe.getHostId());
        } catch (ClientException ce) {
            log.error("ClientException occurred,Error Message:{}", ce.getMessage());
        }
        return null;
    }

    @Override
    public void downloadFile(String path, FileOutputStream outputStream) {
        ConfigVo config = MySetting.getConfig();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = config.getBucketName();
        try (outputStream){
            // ossObject包含文件所在的存储空间名称、文件名称、文件元数据以及一个输入流。
            try (OSSObject ossObject = ossClient.getObject(new GetObjectRequest(bucketName, path).
                    withProgressListener(new ProcessBar.DownloadProgressListener(path)));
                 InputStream inputStream = ossObject.getObjectContent();
                 ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
                 FileChannel fileChannel = outputStream.getChannel()
            ){
                fileChannel.transferFrom(readableByteChannel,0,inputStream.available());
            }catch (OSSException oe) {
                log.error("OSSException occurred,Error Message:{},Code:{},Request ID:{},Host ID:{}",
                        oe.getErrorMessage(),oe.getErrorCode(),oe.getRequestId(), oe.getHostId());
            }
        } catch (Throwable ce) {
            log.error("ClientException,Error Message:{}" , ce.getMessage());
        }
    }
}
