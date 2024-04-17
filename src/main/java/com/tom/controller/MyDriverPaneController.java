package com.tom.controller;

import com.tom.model.ModelData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class MyDriverPaneController extends AnchorPane implements Initializable {

    @FXML
    private AddressPaneController addrController;
    @FXML
    private FileContentPaneController fileContentController;

    @Getter
    private final ModelData modelData;

    public MyDriverPaneController(File file) {
        this.modelData = new ModelData(file);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/MyDriverPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("MyDriverPaneController After loaded");
        modelData.setMyDriverPaneController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("MyDriverPaneController initialize");
        modelData.setAddressPaneController(addrController);
        modelData.setFileContentPaneController(fileContentController);
        addrController.setModelData(modelData);
        fileContentController.setModelData(modelData);
        addrController.doBind();
        fileContentController.bindListener();
    }
}
