package com.tom.handler.icon;

import com.tom.component.center.MainFlowContentPart;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class DesktopIconClickHandler implements EventHandler<MouseEvent> {

    private final ObjectProperty<AnchorPane> os;

    private final Set<AnchorPane> selectedSet;

    private final File file;

    private final MainFlowContentPart mainFlowContentPart;

    public DesktopIconClickHandler(ObjectProperty<AnchorPane> os,Set<AnchorPane> selectedSet,File file, MainFlowContentPart mainFlowContentPart) {
        if (os == null) {
            throw new RuntimeException("os cannot be null!");
        }
        this.os = os;
        this.file = file;
        this.selectedSet = selectedSet;
        this.mainFlowContentPart = mainFlowContentPart;
    }

    @Override
    public void handle(MouseEvent event) {
        System.out.println("iconClickHandler");
        //event.consume();
        AnchorPane curOs = (AnchorPane)event.getSource();
        AnchorPane lastOs = os.get();
        if (lastOs != null) {
            ObservableList<String> styleClass = lastOs.getStyleClass();
            styleClass.remove("my_icon_click");
        }
        curOs.getStyleClass().add("my_icon_click");
        os.set(curOs);
        if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)) {
            if (file.isDirectory()){
                this.mainFlowContentPart.getAddressProperty().setCurPath(file);
                this.mainFlowContentPart.refreshFileNode();
            }else {
                executeFile(file);
            }
        }
    }

    public AnchorPane getOs() {
        return os.get();
    }

    public ObjectProperty<AnchorPane> osProperty() {
        return os;
    }

    public static void executeFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
