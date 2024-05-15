package com.tom.utils;

import com.tom.config.MySetting;
import com.tom.config.SqlSessionInvokeHandler;
import com.tom.config.vo.ConfigVo;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

@Slf4j
public class JDBCUtil {

    /**
     * 测试数据库链接，
     *
     * @return 返回1表示链接成功, 其他失败
     */
    public static int jdbcTest() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ConfigVo configVo = MySetting.getConfig();
        Connection conn = DriverManager.getConnection(configVo.getRemoteDBUrl(), configVo.getRemoteDBUsername(), configVo.getRemoteDBPwd());
        try (conn) {
            PreparedStatement preparedStatement = conn.prepareStatement("select 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            log.error("jdbcTest occurred an error,cause: {}", e.getMessage());
        }
        return 0;
    }


    /**
     * TODO 有待调整，建立mybatis的代理工厂
     */
    public static void createStableConnection() throws Exception {
        var mybatisConfigFilePath = "/config/mybatis-config.xml";

        if (MySetting.getRemoteSessionFactory() == null) {
            int res = JDBCUtil.jdbcTest();
            if (res == 1){
                var inputStream = MySetting.class.getResourceAsStream(mybatisConfigFilePath);
                try (inputStream) {
                    var curSqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "remoteMySQL");
                    MySetting.setRemoteSessionFactory(curSqlSessionFactory);
                } catch (Exception ex) {
                    log.error("createStableConnection RemoteSessionFactory error,cause: ", ex);
                }
            }else {
                throw new RuntimeException("database config error!");
            }
        }


        if (MySetting.getLocalSessionFactory() == null) {
            var inputStream = MySetting.class.getResourceAsStream(mybatisConfigFilePath);
            Properties properties = new Properties();
            properties.put("url",STR."jdbc:sqlite:\{MySetting.getLocalDataPath()}");
            try (inputStream) {
                var curSqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "localSqlLite",properties);
                MySetting.setLocalSessionFactory(curSqlSessionFactory);
            } catch (Exception ex) {
                log.error("createStableConnection LocalSessionFactory error,cause: ", ex);
            }
        }
    }

    public static void closeConnection(){
        if (MySetting.getLocalSessionFactory() != null) {
            MySetting.setLocalSessionFactory(null);
        }
        if (MySetting.getRemoteSessionFactory() != null) {
            closeDataSource(MySetting.getRemoteSessionFactory());
            MySetting.setRemoteSessionFactory(null);
        }
    }

    public static void closeDataSource(SqlSessionFactory curSqlSessionFactory) {
        Environment environment = curSqlSessionFactory.getConfiguration().getEnvironment();
        Object dataSource = environment.getDataSource();
        if (dataSource instanceof HikariDataSource hs) {
            hs.close();
        }
    }


    public static <T>T getRemoteMapper(Class<T> clazz){
        SqlSessionFactory remoteSessionFactory = MySetting.getRemoteSessionFactory();
        if (remoteSessionFactory != null){
            SqlSession sqlSession = remoteSessionFactory.openSession(true);
            T mapper = sqlSession.getMapper(clazz);
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                    new SqlSessionInvokeHandler<>(sqlSession,mapper));
        }
        throw new RuntimeException("MySetting.getMapper occurred an error,remoteSessionFactory didn't initialize properly!");
    }

    public static <T>T getLocalMapper(Class<T> clazz){
        SqlSessionFactory localSessionFactory = MySetting.getLocalSessionFactory();
        if (localSessionFactory != null){
            SqlSession sqlSession = localSessionFactory.openSession(true);
            T mapper = sqlSession.getMapper(clazz);
            return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                    new SqlSessionInvokeHandler<>(sqlSession,mapper));
        }
        throw new RuntimeException("MySetting.getMapper occurred an error,remoteSessionFactory didn't initialize properly!");
    }
}
