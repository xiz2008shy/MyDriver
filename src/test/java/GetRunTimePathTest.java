import org.junit.jupiter.api.Test;

import java.net.URL;

public class GetRunTimePathTest {

    /**
     * 这两种在idea中运行的时候其实都不准确，在jpackage打包后输出的路径是正确的
     */
    @Test
    public void getRunTimePath(){
        URL resource1 = this.getClass().getClassLoader().getResource("");
        System.out.println(resource1);


        String resource2 = System.getProperty("user.dir");
        System.out.println("当前路径：" + resource2);
    }
}
