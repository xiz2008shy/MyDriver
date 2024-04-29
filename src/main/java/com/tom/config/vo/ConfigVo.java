package com.tom.config.vo;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * oss服务商
     */
    private String ossProvider;
    /**
     * bucket名称
     */
    private String bucketName;
    /**
     * 服务端区域地址
     */
    private String ossEndpoint;
    /**
     * 访问id
     */
    private String accessKeyId;
    /**
     * 访问密钥
     */
    private String accessKeySecret;



    public <T>T getValue(String key) {
        return (T)ReflectUtil.getFieldValue(this,key);
    }

    @JsonIgnore
    private Map<String,String> cacheMap = new HashMap<>();

    public void saveBak(){
        Field[] fields = ReflectUtil.getFields(ConfigVo.class);
        for (Field field : fields) {
            if (!field.getName().equals("cacheMap")) {
                Object value = ReflectUtil.getFieldValue(this, field);
                if (value instanceof String s){
                    this.cacheMap.put(field.getName(),s);
                }
            }
        }
    }


    public void restore(){
        for (Map.Entry<String, String> entry : this.cacheMap.entrySet()) {
            ReflectUtil.setFieldValue(this,entry.getKey(),entry.getValue());
        }
    }

}
