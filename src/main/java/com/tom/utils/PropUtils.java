package com.tom.utils;

import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.model.PropData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropUtils {

    public static PropData aliyunOssRegion(){
        return readFromProperties("/properties/aliyunOssRegion.properties");
    }

    public static PropData ossProvider(){
        return readFromProperties("/properties/ossProvider.properties");
    }

    private static PropData readFromProperties(String propPath) {
        ObservableList<Map.Entry<Object, String>> list = FXCollections.observableArrayList();
        Properties properties = new Properties();
        try(InputStream inputStream = MySetting.class.getResourceAsStream(propPath)) {
            properties.load(inputStream);
            for (Map.Entry entry : properties.entrySet()) {
                list.add(entry);
            }
        } catch (IOException e) {
            log.error("PropUtils.aliyunOssRegion occurred an error:",e);
        }
        list.sort(Comparator.comparing(x -> x.getValue().toString()));
        return new PropData(properties,list);
    }

    @Getter
    private static StringConverter<Map.Entry<Object, String>> propConverter = new StringConverter<>() {
        @Override
        public String toString(Map.Entry<Object, String> entry) {
            if (entry != null) {
                return entry.getValue();
            }
            return StrUtil.EMPTY;
        }

        @Override
        public Map.Entry<Object, String> fromString(String s) {
            return null;
        }
    };
}
