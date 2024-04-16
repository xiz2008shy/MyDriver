package com.tom.controller;

import com.tom.model.ModelData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        System.out.println("FileContentPaneController initialized");
    }

    /**
     * 绑定监听器，当modelData的curDir发生变化时，变更浏览内容
     */
    public void bindListener(){
        changeContentWhenChangeDir(modelData.getCurDir().get());
        modelData.getCurDir().addListener((e,o,n) -> {
            changeContentWhenChangeDir(n);
        });
    }

    private void changeContentWhenChangeDir(File n) {
        flowPaneContent.getChildren().clear();
        File[] files = n.listFiles();
        assert files != null;
        for (File file : files) {
            addFileNode(file);
        }
    }

    private void addFileNode (File file) {
        FileViewController fileViewController = new FileViewController(file,modelData);
        flowPaneContent.getChildren().add(fileViewController);
    }


}
