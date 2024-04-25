package com.tom.utils;

import cn.hutool.core.util.StrUtil;

import java.io.File;

public class FileNameUtil {

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
}
