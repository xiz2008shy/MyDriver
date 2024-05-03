package com.tom.oss;

import java.io.File;
import java.io.FileOutputStream;

public interface OssOperation {

    <T>T uploadFile(String filePath, File toUpload);

    void downloadFile(String path, FileOutputStream outputStream);
}
