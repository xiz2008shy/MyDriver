package com.tom.controller;

import com.tom.model.ModelData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class FileContentPaneController implements Initializable {

    @FXML
    private FlowPane flowPaneContent;

    @Getter
    @Setter
    private ModelData modelData;


    public FileContentPaneController() throws IOException {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("FileContentPaneController initialized");
    }

    /**
     * 绑定监听器，当modelData的curDir发生变化时，变更浏览内容
     */
    public void bindListener(){
        changeContentWhenChangeDir(modelData.getCurDirProperty().get());
        modelData.getCurDirProperty().addListener((_,_,n) -> changeContentWhenChangeDir(n));
    }

    /**
     * 监听方法，当路径变更时，变更当前展示内容
     * @param n
     */
    private void changeContentWhenChangeDir(File n) {
        flowPaneContent.getChildren().clear();
        File[] files = n.listFiles();
        assert files != null;
        modelData.getCacheMap().clear();
        // 如果n是某些系统隐藏文件时，可能导致files为null
        if (files != null){
            for (File file : files) {
                addFileNode(file);
            }
        }

    }

    private void addFileNode (File file) {
        FileViewController fileViewController = new FileViewController(file,modelData);
        flowPaneContent.getChildren().add(fileViewController);
    }


}
