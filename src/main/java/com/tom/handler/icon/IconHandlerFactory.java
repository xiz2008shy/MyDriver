package com.tom.handler.icon;

import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class IconHandlerFactory<T> {

    private ObjectProperty<AnchorPane> os;

    private Set<AnchorPane> selectedSet;

    private File file;

    private IconClickHandler<T> iconClickHandler;
    private IconMouseInHandler<T> iconMouseInHandler;
    private IconMouseOutHandler<T> iconMouseOutHandler;


    public IconHandlerFactory(ObjectProperty<AnchorPane> os, Set<AnchorPane> selectedSet) {
        this.os = os;
        this.selectedSet = selectedSet;
    }


    public void makeHandleInstance(File file){
        this.iconClickHandler = new IconClickHandler<>(os,selectedSet,file);
        this.iconMouseInHandler = new IconMouseInHandler<>(iconClickHandler);
        this.iconMouseOutHandler = new IconMouseOutHandler<>(iconClickHandler);
    }


    public IconClickHandler<T> getIconClickHandler() {
        return iconClickHandler;
    }

    public IconMouseInHandler<T> getIconMouseInHandler() {
        return iconMouseInHandler;
    }

    public IconMouseOutHandler<T> getIconMouseOutHandler() {
        return iconMouseOutHandler;
    }


    /**
     * 鼠标移入图标事件
     * @param <T>
     */
    static class IconMouseInHandler <T> implements EventHandler {
        private IconClickHandler<T> iconClickHandler;

        public IconMouseInHandler(IconClickHandler<T> iconClickHandler) {
            this.iconClickHandler = iconClickHandler;
        }

        @Override
        public void handle(Event event) {
            Pane source = (Pane) event.getSource();
            if (!source.equals(iconClickHandler.getOs())) {
                source.setStyle("-fx-background-color: #dce9f1");
            }
        }
    }

    /**
     * 鼠标移出图标事件
     * @param <T>
     */
    static class IconMouseOutHandler<T> implements EventHandler {

        private IconClickHandler<T> iconClickHandler;

        public IconMouseOutHandler(IconClickHandler<T> iconClickHandler) {
            this.iconClickHandler = iconClickHandler;
        }

        @Override
        public void handle(Event event) {
            Pane source = (Pane)event.getSource();
            if (!source.equals(iconClickHandler.getOs())) {
                source.setStyle("-fx-background-color: none");
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

        public IconClickHandler(ObjectProperty<AnchorPane> os,Set<AnchorPane> selectedSet,File file) {
            if (os == null) {
                throw new RuntimeException("os cannot be null!");
            }
            this.os = os;
            this.file = file;
            this.selectedSet = selectedSet;
        }

        @Override
        public void handle(MouseEvent event) {
            AnchorPane curOs = (AnchorPane)event.getSource();
            AnchorPane lastOs = os.get();
            if (lastOs != null) {
                lastOs.setStyle("-fx-background-color: none");
            }
            curOs.setStyle("-fx-background-color: rgba(63,175,229,0.3)");
            os.set(curOs);
            if (event.getClickCount() == 2) {
                if (file.isDirectory()){

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
    }


    public static void executeFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
