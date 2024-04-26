package com.tom.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tom.config.vo.ConfigVo;
import com.tom.controller.MySettingController;
import com.tom.general.RecWindows;
import com.tom.utils.JdbcUtil;
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
import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Getter @Setter
    private static SqlSessionFactory remoteSessionFactory;

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
                fromWindows.getBaseMenu().closeMenu(null);
                fromWindows.getStatusMenu().closeMenu(null);
                // 弹出设置窗口
                new MySettingController(fromWindows);
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
        if (e.getButton().equals(MouseButton.PRIMARY) && configChange) {
            boolean isBasePathValid = mySettingController.validBasePath();
            if (isBasePathValid) {
                mySettingController.getConfigChange().set(0);
                mySettingController.refreshConfig();
                Path path = Paths.get(runConfFile);
                try {
                    String configStr = OM.writeValueAsString(config);
                    createFileWithConfig(path,configStr);
                } catch (Exception ex) {
                    log.error("MySetting.saveConfig occurred an error,cause: ",ex);
                }
            }else {
                mySettingController.showDialog("同步路径不存在，请重新填写","The path is not exists！");
            }
        }
    }


    public static EventHandler<MouseEvent> testConnection(MySettingController mySettingController) {
        return e -> {
            e.consume();
            mySettingController.disableTestConnection();
            mySettingController.clearTestImg();
            mySettingController.loseFocused(null);
            mySettingController.setTestImgLoading();
            Thread.startVirtualThread(() -> {
                asyncTestConnection(mySettingController);
            });
        };
    }

    private static void asyncTestConnection(MySettingController mySettingController) {
        MySetting.getConfig().saveBak();
        mySettingController.refreshConfig();
        doJdbcConnection(mySettingController);
        Platform.runLater(mySettingController::restoreTestConnection);
    }


    private static void doJdbcConnection(MySettingController mySettingController) {
        int res;
        try {
            res = JdbcUtil.jdbcTest();
            if (res == 1){
                mySettingController.setTestImgRight();
                log.info("jdbc test Connection success!");
            }else {
                MySetting.getConfig().restore();
                Platform.runLater(() -> {
                    mySettingController.setTestImgError();
                    mySettingController.showDialog("链接失败，请稍后再试！","connection failed!");
                });
            }
        } catch (Exception e) {
            MySetting.getConfig().restore();
            Platform.runLater(() -> {
                mySettingController.setTestImgError();
                mySettingController.showDialog(e.getMessage() ,"Connection Failed!");
            });
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
