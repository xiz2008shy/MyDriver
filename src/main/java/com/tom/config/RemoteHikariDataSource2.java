package com.tom.config;

import com.tom.config.vo.ConfigVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class RemoteHikariDataSource2 {

    private HikariDataSource dataSource;

    public RemoteHikariDataSource2()  {
        Properties hikariProperties = new Properties();
        try {
            hikariProperties.load(this.getClass().getResourceAsStream("/config/hikariConfig.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HikariConfig config = new HikariConfig(hikariProperties);
        ConfigVo configVo = MySetting.getConfig();
        /*config.setDriverClassName("com.mysql.cj.jdbc.Driver");*/
        config.setJdbcUrl(configVo.getRemoteDBUrl());
        config.setUsername(configVo.getRemoteDBUsername());
        config.setPassword(configVo.getRemoteDBPwd());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.dataSource = new HikariDataSource(config);
        }catch (Exception e){
            log.error("RemoteHikariDataSource2 constructor occurred an error,cause: ",e);
        }
    }


    public boolean testConnection()  {
        try(Connection connection = this.dataSource.getConnection();){
            connection.setAutoCommit(true);
            PreparedStatement preparedStatement = connection.prepareStatement("select 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int res = resultSet.getInt(1);
                if (res == 1){
                    return true;
                }
                log.info("testConnection success and receive 1");
            }else {
                log.error("testConnection failed, has no result");
            }
        }catch (Exception e){
            log.error("RemoteHikariDataSource2.testConnection occurred an error,cause: ",e);
        }
        return false;
    }
}
