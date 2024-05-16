package com.tom;

import com.tom.config.MySetting;
import com.tom.mapper.LocalRecordMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MT {

    public static void main(String[] args) throws IOException {
        MySetting.initSetting(MySetting.mockParam());
        String resource = "/config/mybatis-config.xml";
        InputStream inputStream = MT.class.getResourceAsStream(resource);
        Properties properties = new Properties();
        properties.put("url",STR."jdbc:sqlite:\{MySetting.getLocalDataPath()}");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,"localSqlLite",properties);
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            LocalRecordMapper mapper = session.getMapper(LocalRecordMapper.class);
            mapper.createTableIfNotExists();
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
