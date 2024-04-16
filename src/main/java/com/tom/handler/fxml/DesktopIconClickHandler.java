package com.tom.handler.fxml;

import com.tom.model.ModelData;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class DesktopIconClickHandler implements EventHandler<MouseEvent> {

    private final File file;

    private final ModelData modelData;

    public DesktopIconClickHandler(File file, ModelData modelData) {
        this.file = file;
        this.modelData = modelData;
    }

    @Override
    public void handle(MouseEvent event) {
        System.out.println("iconClickHandler");
        //event.consume();
        AnchorPane curOs = (AnchorPane)event.getSource();
        AnchorPane lastOs = modelData.getSelectedFile();
        if (lastOs != null) {
            ObservableList<String> styleClass = lastOs.getStyleClass();
            styleClass.remove("my_icon_click");
        }
        curOs.getStyleClass().add("my_icon_click");
        modelData.setSelectedFile(curOs);
        modelData.setRealSelectedFile(file);
        if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)) {
            if (file.isDirectory()){
                this.modelData.setFile(file);
            }else {
                executeFile(file);
            }
        }
    }


    public static void executeFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}