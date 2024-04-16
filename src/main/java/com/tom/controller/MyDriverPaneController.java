package com.tom.controller;

import com.tom.model.ModelData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyDriverPaneController extends AnchorPane implements Initializable {

    @FXML
    private AddressPaneController addrController;
    @FXML
    private FileContentPaneController fileContentController;

    private final ModelData modelData;

    public MyDriverPaneController(File file) throws IOException {
        this.modelData = new ModelData(file);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/MyDriverPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
        System.out.println("MyDriverPaneController After loaded");
        modelData.setMyDriverPaneController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MyDriverPaneController initialize");
        modelData.setAddressPaneController(addrController);
        modelData.setFileContentPaneController(fileContentController);
        addrController.setModelData(modelData);
        fileContentController.setModelData(modelData);
        addrController.doBind();
        fileContentController.bindListener();
    }
}
