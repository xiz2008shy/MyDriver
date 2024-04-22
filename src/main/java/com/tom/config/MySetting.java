package com.tom.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tom.config.vo.ConfigVo;
import com.tom.controller.MySettingController;
import com.tom.general.RecWindows;
import com.tom.mapper.FileRecordMapper;
import com.tom.mybatis.MySqlSessionFactoryBuilder;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.*;


public class MySetting {

    @Getter
    private static ConfigVo config;

    private static Logger log = null;

    /**
     * 运行时配置文件路径
     */
    private static String runConfFile;
    /**
     * 运行时本地数据路径
     */
    private static String localDataPath;

    private static SqlSessionFactory localSessionFactory;
    private static SqlSessionFactory remoteSessionFactory;

    private static RemoteHikariDataSource2 dataSource2;

    private static final ObjectMapper OM = new ObjectMapper();

    static {
        // 配置ObjectMapper以启用格式化输出（缩进）
        OM.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
        OM.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void initSetting(Application.Parameters parameters)  {
        Map<String, String> namedArgs = parameters.getNamed();
        String debug = namedArgs.get("debug");
        String runLogDir;
        // 当前运行时目录
        String curDir = System.getProperty("user.dir");
        if ("1".equals(debug)) {
            runConfFile = STR."\{curDir }\{File.separator}.conf\{File.separator}setting.json";
            runLogDir = STR."\{curDir }\{File.separator}target\{File.separator}log";
            localDataPath = STR."\{curDir }\{File.separator}data\{File.separator}fileOd.db";
        }else {
            runConfFile = STR."\{curDir }\{File.separator}conf\{File.separator}setting.json";
            runLogDir = STR."\{curDir }\{File.separator}log";
            localDataPath = STR."\{curDir }\{File.separator}data\{File.separator}fileOd.db";
        }

        // 设置日志路径
        System.setProperty("LogHomeRoot", runLogDir);
        log = LoggerFactory.getLogger(MySetting.class);
        Path path = Paths.get(runConfFile);
        try {
            if (Files.exists(path) && Files.isRegularFile(path)) {
                byte[] bytes = Files.readAllBytes(path);
                ObjectMapper objectMapper = new ObjectMapper();
                MySetting.config = objectMapper.readValue(bytes, ConfigVo.class);
            }else {
                File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
                String desktopPath = desktopDir.getAbsolutePath();
                MySetting.config = new ConfigVo().setBasePath(desktopPath);
                String configStr = OM.writeValueAsString(config);
                createFileWithConfig(path,configStr);
            }
        } catch (IOException e) {
            log.error("MySetting.initSetting occurred an error,cause: ",e);
        }
    }


    public static void createFileWithConfig(Path path,String config) throws IOException {
        checkParent(path);
        Files.writeString(path, config, WRITE,CREATE,TRUNCATE_EXISTING);
    }

    private static void checkParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (!Files.exists(parent)){
            checkParent(parent);
            Files.createDirectories(parent);
        }
    }



    public static EventHandler<MouseEvent> openSettingPane(Pane clickButton,RecWindows fromWindows) {
        return e -> {
            e.consume();
            if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED) && (clickButton.equals(e.getPickResult().getIntersectedNode()) ||
                    clickButton.getChildren().getFirst().equals(e.getPickResult().getIntersectedNode()))) {
                Stage utility = new Stage();
                utility.initStyle(StageStyle.UTILITY);
                utility.setOpacity(0);
                Stage settingStage = new Stage();
                MySettingController mySettingController = new MySettingController();
                RecWindows settingWindows = new RecWindows(mySettingController, 600.0,
                        600.0, 12.0, settingStage,"setting",1);
                settingWindows.setFromWindows(fromWindows);
                settingWindows.initStage();
                mySettingController.setWindows(settingWindows);
                settingStage.initModality(Modality.APPLICATION_MODAL);
                settingStage.initOwner(utility);
                utility.show();
                settingStage.show();
            }
        };
    }


    public static EventHandler<MouseEvent> okBtnClick(MySettingController mySettingController) {
        return e -> {
            e.consume();
            mySettingController.getWindows().close();
            saveConfigToFile(mySettingController, e);
        };
    }


    public static EventHandler<MouseEvent> applyBtnClick(MySettingController mySettingController) {
        return e -> {
            e.consume();
            mySettingController.loseFocused(null);
            saveConfigToFile(mySettingController, e);
        };
    }


    private static void saveConfigToFile(MySettingController mySettingController, MouseEvent e) {
        boolean configChange = mySettingController.isConfigChange();
        mySettingController.getConfigChange().set(0);
        if (e.getButton().equals(MouseButton.PRIMARY) && configChange) {
            mySettingController.refreshConfig();
            Path path = Paths.get(runConfFile);
            try {
                String configStr = OM.writeValueAsString(config);
                createFileWithConfig(path,configStr);
            } catch (Exception ex) {
                log.error("MySetting.saveConfig occurred an error,cause: ",ex);
            }
        }
    }


    public static EventHandler<MouseEvent> testConnection(MySettingController mySettingController) {
        return e -> {
            e.consume();
            mySettingController.disableTestConnection();
            mySettingController.clearTestImg();
            mySettingController.loseFocused(null);
            Thread.startVirtualThread(() -> {
                asyncTestConnection(mySettingController);
            });
        };
    }

    private static void asyncTestConnection(MySettingController mySettingController) {
        MySetting.getConfig().saveBak();
        mySettingController.refreshConfig();
        doConnectionTest1(mySettingController);

        Platform.runLater(mySettingController::restoreTestConnection);
    }


    private static void doConnectionTest2(MySettingController mySettingController) {
        RemoteHikariDataSource2 dataSource = new RemoteHikariDataSource2();
        boolean testRes = dataSource.testConnection();
        if (testRes){
            mySettingController.setTestImgRight();
            if (MySetting.dataSource2 == null){
                MySetting.dataSource2 = dataSource;
            }
            log.info("testConnection success");
        }else {
            MySetting.getConfig().restore();
        }
    }

    private static void doConnectionTest1(MySettingController mySettingController) {
        var mybatisConfigFilePath = "/config/mybatis-config.xml";
        var inputStream = MySetting.class.getResourceAsStream(mybatisConfigFilePath);
        try(inputStream) {
            var curSqlSessionFactory = new MySqlSessionFactoryBuilder().build(inputStream,"remoteMySQL");
            SqlSession session = curSqlSessionFactory.openSession();
            String res;
            try (session) {
                FileRecordMapper mapper = session.getMapper(FileRecordMapper.class);
                res = mapper.test();
            }

            if (res.equals("1")){
                if(MySetting.remoteSessionFactory == null){
                    MySetting.remoteSessionFactory = curSqlSessionFactory;
                }else {
                    closeDataSource(curSqlSessionFactory);
                }
                mySettingController.setTestImgRight();
                log.info("testConnection success");
            }else {
                MySetting.getConfig().restore();
            }
        }catch (Exception ex){
            log.error("testConnection error,cause: ",ex);
            log.info("testConnection error,cause: {}",ex.getMessage());
        }
    }

    private static void closeDataSource(SqlSessionFactory curSqlSessionFactory) {
        Environment environment = curSqlSessionFactory.getConfiguration().getEnvironment();
        Object dataSource = environment.getDataSource();
        if (dataSource instanceof HikariDataSource hs) {
            hs.close();
        }
    }


    private static final Application.Parameters mockParam = new Application.Parameters() {
        private Map<String,String> namedMap = new HashMap<>();
        {
            namedMap.put("debug","1");
        }
        @Override
        public List<String> getRaw() {
            return null;
        }

        @Override
        public List<String> getUnnamed() {
            return null;
        }

        @Override
        public Map<String, String> getNamed() {
            return namedMap;
        }
    };

    public static Application.Parameters mockParam(){
        return mockParam;
    }
}
