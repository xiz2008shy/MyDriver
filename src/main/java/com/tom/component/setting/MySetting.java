package com.tom.component.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.component.console.MyLogListPane;
import com.tom.general.RecWindows;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class MySetting {

    @Getter
    private static ConfigEntity config;

    public static void initSetting(Application.Parameters parameters)  {
        Map<String, String> namedArgs = parameters.getNamed();
        ListView<String> listView = MyLogListPane.getListView();
        String debug = namedArgs.get("debug");
        String runConfFile;
        String runLogDir;
        if ("1".equals(debug)) {
            listView.getItems().add(STR."debug=\{debug}");
            runConfFile = STR."\{System.getProperty("user.dir")}\{File.separator}target\{File.separator}conf\{File.separator}setting.json";
            runLogDir = STR."\{System.getProperty("user.dir")}\{File.separator}target\{File.separator}log";
        }else {
            runConfFile = STR."\{System.getProperty("user.dir")}\{File.separator}conf\{File.separator}setting.json";
            runLogDir = STR."\{System.getProperty("user.dir")}\{File.separator}log";
        }

        System.setProperty("LogHomeRoot", runLogDir);
        listView.getItems().add(STR."runConfFile=\{runConfFile}");
        Path path = Paths.get(runConfFile);
        try {
            if (Files.exists(path) && Files.isRegularFile(path)) {
                byte[] bytes = Files.readAllBytes(path);
                ObjectMapper objectMapper = new ObjectMapper();
                MySetting.config = objectMapper.readValue(bytes, ConfigEntity.class);
            }else {
                File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
                String desktopPath = desktopDir.getAbsolutePath();
                MySetting.config = new ConfigEntity().setBasePath(desktopPath);
                String configStr = new ObjectMapper().writeValueAsString(config);
                createFileWithConfig(path,configStr);
            }
        } catch (IOException e) {
            listView.getItems().add(e.getMessage());
        }
    }


    public static void createFileWithConfig(Path path,String config) throws IOException {
        checkParent(path);
        Files.writeString(path, config, WRITE,CREATE);
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
                RecWindows settingWindows = new RecWindows(new Pane(), 600.0,
                        600.0, 12.0, settingStage,"setting",1);
                settingWindows.setFromWindows(fromWindows);
                settingWindows.initStage();
                settingStage.initModality(Modality.APPLICATION_MODAL);
                settingStage.initOwner(utility);
                utility.show();
                settingStage.show();
            }
        };
    }



}
