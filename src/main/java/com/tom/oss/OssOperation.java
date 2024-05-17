package com.tom.oss;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public interface OssOperation {

    <T>T uploadFile(String filePath, File toUpload);

    void downloadFile(String path, FileChannel outChannel);
}
