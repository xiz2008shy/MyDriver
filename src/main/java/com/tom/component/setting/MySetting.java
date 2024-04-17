package com.tom.component.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.component.console.MyLogListPane;
import javafx.application.Application;
import javafx.scene.control.ListView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class MySetting {

    private static ConfigEntity configEntity;

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
                ConfigEntity config = objectMapper.readValue(bytes, ConfigEntity.class);
                configEntity = config;
            }else {
                File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
                String desktopPath = desktopDir.getAbsolutePath();
                configEntity = new ConfigEntity().setBasePath(desktopPath);
                String config = new ObjectMapper().writeValueAsString(configEntity);
                createFileWithConfig(path,config);
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

    public static ConfigEntity getConfig(){
        return configEntity;
    }

}
