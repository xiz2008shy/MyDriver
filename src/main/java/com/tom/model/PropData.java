package com.tom.model;

import cn.hutool.core.util.StrUtil;
import javafx.collections.ObservableList;

import java.util.Map;
import java.util.Properties;


public record PropData(Properties properties, ObservableList<Map.Entry<Object, String>> list) {

    public Map.Entry<Object, String> getProperty(String key) {
        return getProperty(key, null);
    }

    public Map.Entry<Object, String> getProperty(String key, String defaultValue) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        String value = properties.get(key) == null ? defaultValue : (String) properties.get(key);
        return new Map.Entry<>() {
            @Override
            public Object getKey() {
                return key;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public String setValue(String value) {
                return null;
            }
        };
    }
}
