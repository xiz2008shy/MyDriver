package com.tom;

import com.tom.config.MySetting;
import com.tom.entity.FileRecord;
import com.tom.mapper.FileRecordMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class MT {

    public static void main(String[] args) throws IOException {
        MySetting.initSetting(MySetting.mockParam());
        String resource = "/config/mybatis-config.xml";
        InputStream inputStream = MT.class.getResourceAsStream(resource);
        Properties properties = new Properties();
        properties.put("url","jdbc:sqlite:sample.db");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,properties);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FileRecordMapper mapper = session.getMapper(FileRecordMapper.class);
            List<FileRecord> fileRecords = mapper.selectList();
            System.out.println(fileRecords);
        }

        String resource2 = "/config/mybatis-config.xml";
        InputStream inputStream2 = MT.class.getResourceAsStream(resource);

        SqlSessionFactory sqlSessionFactory2 = new SqlSessionFactoryBuilder().build(inputStream2,"remoteMySQL");

        try (SqlSession session = sqlSessionFactory2.openSession()) {
            FileRecordMapper mapper = session.getMapper(FileRecordMapper.class);
            List<FileRecord> fileRecords = mapper.selectList();
            System.out.println(fileRecords);
        }
    }
}
