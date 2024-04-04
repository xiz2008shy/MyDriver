package com.tom.handler.icon;

import com.tom.component.center.MainFlowContentPart;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class IconHandlerFactory<T> {

    private ObjectProperty<AnchorPane> os;

    private Set<AnchorPane> selectedSet;

    private IconClickHandler<T> iconClickHandler;
    private IconMouseInOutHandler<T> iconMouseInOutHandler;


    public IconHandlerFactory(ObjectProperty<AnchorPane> os, Set<AnchorPane> selectedSet) {
        this.os = os;
        this.selectedSet = selectedSet;
    }


    public void makeHandleInstance(File file, MainFlowContentPart mainFlowContentPart){
        this.iconClickHandler = new IconClickHandler<>(os,selectedSet,file,mainFlowContentPart);
        this.iconMouseInOutHandler = new IconMouseInOutHandler<>(iconClickHandler);
    }


    public IconClickHandler<T> getIconClickHandler() {
        return iconClickHandler;
    }

    public IconMouseInOutHandler<T> getIconInOutHandler() {
        return iconMouseInOutHandler;
    }


    /**
     * 鼠标移入图标事件
     * @param <T>
     */
    static class IconMouseInOutHandler <T> implements EventHandler<MouseEvent> {
        private IconClickHandler<T> iconClickHandler;

        private boolean flag = true;

        public IconMouseInOutHandler(IconClickHandler<T> iconClickHandler) {
            this.iconClickHandler = iconClickHandler;
        }

        @Override
        public void handle(MouseEvent event) {
            Region source = (Region) event.getSource();
            if (!source.equals(iconClickHandler.getOs())) {
                if (flag) {
                    source.setStyle("-fx-background-color: #dce9f1");
                }else {
                    source.setStyle("-fx-background-color: none");
                }
                this.flag = !flag;
            }else {
                this.flag = true;
            }
        }
    }


    /**
     * 鼠标单机图标事件
     * @param <T>
     */
    static class IconClickHandler<T> implements EventHandler<MouseEvent> {

        private ObjectProperty<AnchorPane> os;

        private Set<AnchorPane> selectedSet;

        private File file;

        private MainFlowContentPart mainFlowContentPart;

        public IconClickHandler(ObjectProperty<AnchorPane> os,Set<AnchorPane> selectedSet,File file, MainFlowContentPart mainFlowContentPart) {
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
            if(event.getButton().equals(MouseButton.PRIMARY)){
                AnchorPane curOs = (AnchorPane)event.getSource();
                AnchorPane lastOs = os.get();
                if (lastOs != null) {
                    lastOs.setStyle("-fx-background-color: none");
                }
                curOs.setStyle("-fx-background-color: rgba(63,175,229,0.3)");
                os.set(curOs);
                if (event.getClickCount() == 2) {
                    if (file.isDirectory()){
                        this.mainFlowContentPart.getAddressProperty().setCurPath(file);
                        this.mainFlowContentPart.refreshFileNode();
                    }else {
                        executeFile(file);
                    }
                }
            }
        }

        public AnchorPane getOs() {
            return os.get();
        }

        public ObjectProperty<AnchorPane> osProperty() {
            return os;
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
