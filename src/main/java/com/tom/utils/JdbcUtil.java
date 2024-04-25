package com.tom.utils;

import com.tom.config.MySetting;
import com.tom.config.vo.ConfigVo;
import com.tom.controller.MySettingController;
import com.tom.mapper.FileRecordMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

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


    /**
     * TODO 有待调整，建立mybatis的代理工厂
     * @param mySettingController
     */
    public static void createStableConnection(MySettingController mySettingController) {
        var mybatisConfigFilePath = "/config/mybatis-config.xml";
        var inputStream = MySetting.class.getResourceAsStream(mybatisConfigFilePath);
        try(inputStream) {
            var curSqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,"remoteMySQL");
            SqlSession session = curSqlSessionFactory.openSession();
            String res;
            try (session) {
                FileRecordMapper mapper = session.getMapper(FileRecordMapper.class);
                res = mapper.test();
            }

            if (res.equals("1")){
                if(MySetting.getRemoteSessionFactory() == null){
                    MySetting.setRemoteSessionFactory(curSqlSessionFactory);
                }else {
                    closeDataSource(curSqlSessionFactory);
                }
                mySettingController.setTestImgRight();
                log.info("createStableConnection success");
            }else {
                MySetting.getConfig().restore();
            }
        }catch (Exception ex){
            log.error("createStableConnection error,cause: ",ex);
            log.info("createStableConnection error,cause: {}",ex.getMessage());
        }
    }

    private static void closeDataSource(SqlSessionFactory curSqlSessionFactory) {
        Environment environment = curSqlSessionFactory.getConfiguration().getEnvironment();
        Object dataSource = environment.getDataSource();
        if (dataSource instanceof HikariDataSource hs) {
            hs.close();
        }
    }
}
