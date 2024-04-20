package com.tom.config.vo;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigVo implements Serializable {

    /**
     * 同步路径
     */
    private String basePath;
    /**
     * 远端数据库jdbc数据库url
     */
    private String remoteDBUrl;
    /**
     * 远端数据库用户名
     */
    private String remoteDBUsername;
    /**
     * 远端数据库密码
     */
    private String remoteDBPwd;


    public <T>T getValue(String key) {
        return (T)ReflectUtil.getFieldValue(this,key);
    }

}
