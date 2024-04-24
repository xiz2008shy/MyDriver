package com.tom.utils;

import com.tom.config.MySetting;
import com.tom.config.vo.ConfigVo;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
public class JdbcUtil {

    /**
     * 测试数据库链接，
     * @return 返回1表示链接成功,其他失败
     */
    public static int jdbcTest() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        ConfigVo configVo = MySetting.getConfig();
        Connection conn = DriverManager.getConnection(configVo.getRemoteDBUrl(), configVo.getRemoteDBUsername(),configVo.getRemoteDBPwd());
        try(conn) {
            PreparedStatement preparedStatement = conn.prepareStatement("select 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
        }catch (Exception e){
            log.error("jdbcTest occurred an error,cause: {}",e.getMessage());
        }
        return 0;
    }
}
