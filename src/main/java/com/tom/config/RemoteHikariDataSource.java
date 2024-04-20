package com.tom.config;

import com.tom.config.vo.ConfigVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import java.io.IOException;
import java.util.Properties;

public class RemoteHikariDataSource extends UnpooledDataSourceFactory {

    public RemoteHikariDataSource() throws IOException {
        Properties hikariProperties = new Properties();
        hikariProperties.load(this.getClass().getResourceAsStream("/config/hikariConfig.properties"));
        HikariConfig config = new HikariConfig(hikariProperties);
        ConfigVo configVo = MySetting.getConfig();
        config.setJdbcUrl(configVo.getRemoteDBUrl());
        config.setUsername(configVo.getRemoteDBUsername());
        config.setPassword(configVo.getRemoteDBPwd());

        this.dataSource = new HikariDataSource(config);
    }

}
