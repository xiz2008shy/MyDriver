import com.tom.entity.FileRecord;
import com.tom.mapper.FileRecordMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyBatisTest {

    @Test
    public void test() throws IOException {
        String resource = "/config/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FileRecordMapper mapper = session.getMapper(FileRecordMapper.class);
            List<FileRecord> fileRecords = mapper.selectListByRelativeLocation("/");
            System.out.println(fileRecords);
        }
    }
}
