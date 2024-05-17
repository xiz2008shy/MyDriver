package com.tom.utils;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.core.text.StrPool.DOT;

@Slf4j
public class FileNameUtil {

    /**
     * 处理文件名称， 针对windows的根盘符进行特别处理
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        String labelName = file.getName();
        if (StrUtil.isBlank(labelName)){
            labelName = file.getAbsolutePath();
            if (StrUtil.contains(labelName,":")){
                labelName = labelName.substring(0,labelName.indexOf(":") + 1);
            }
        }
        return labelName;
    }

    private final static Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 判断是否包含中文
     * @param path 需要判断的路径
     * @return true 表示包含中文
     */
    public static boolean containChinese(String path)
    {
        Matcher m = p.matcher(path);
        return m.find();
    }


    /**
     * 获取相对于基础路径的相对路径
     * @param dir
     * @param basePath
     * @return
     */
    public static String getRelativePath(File dir, String basePath) {
        return STR."\{dir.getAbsolutePath().replace(basePath, StrUtil.EMPTY).replaceAll("\\\\", "/")}/";
    }


    public static Pair<String,String> parseFilename(String fileName) {
        int i = fileName.lastIndexOf(DOT);
        if (i != -1) {
            String pureName = fileName.substring(0,i);
            String extName = fileName.substring(i);
            return Pair.of(pureName,extName);
        }else {
            return Pair.of(fileName,StrUtil.EMPTY);
        }
    }


    public static FileChannel createFileChannelCEW(String path) throws IOException {
        return FileChannel.open(Paths.get(path),
                StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE);
    }

    public static FileChannel createFileChannelCEW(Path path) throws IOException {
        return FileChannel.open(path,
                StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE);
    }
}
