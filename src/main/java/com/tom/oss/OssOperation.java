package com.tom.oss;

import java.io.InputStream;

public interface OssOperation {

    public <T>T uploadFile(String filePath, InputStream inputStream);
}
