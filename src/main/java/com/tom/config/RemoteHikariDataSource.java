package com.tom.config;

import com.tom.config.vo.ConfigVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class RemoteHikariDataSource extends UnpooledDataSourceFactory {

    public RemoteHikariDataSource() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Properties hikariProperties = new Properties();
        hikariProperties.load(this.getClass().getResourceAsStream("/config/hikariConfig.properties"));
        HikariConfig config = new HikariConfig(hikariProperties);
        ConfigVo configVo = MySetting.getConfig();
        /*config.setDriverClassName("com.mysql.cj.jdbc.Driver");*/
        config.setJdbcUrl(configVo.getRemoteDBUrl());
        config.setUsername(configVo.getRemoteDBUsername());
        config.setPassword(configVo.getRemoteDBPwd());

        try {
            this.dataSource = new HikariDataSource(config);
        }catch (Exception e){
            log.error("RemoteHikariDataSource occurred an error,cause: ",e);
        }
    }

}
