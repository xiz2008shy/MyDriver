package com.tom.handler;

import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.Set;

/**
 * 图标事件的工厂方法
 */
public class IconHandlerFactory<T extends Event> {

    private ObjectProperty<AnchorPane> os;

    private Set<AnchorPane> selectedSet;

    private IconClickHandler<T>iconClickHandler;
    private IconMouseInHandler<T> iconMouseInHandler;
    private IconMouseOutHandler<T> iconMouseOutHandler;

    public static <T extends Event>IconHandlerFactory<T> getInstance(ObjectProperty<AnchorPane> os, Set<AnchorPane> selectedSet){
        return new IconHandlerFactory<>(os,selectedSet);
    }

    private IconHandlerFactory(ObjectProperty<AnchorPane> os, Set<AnchorPane> selectedSet) {
        this.os = os;
        this.selectedSet = selectedSet;
    }


    public void makeHandleInstance(){
        this.iconClickHandler = new IconClickHandler<>(os,selectedSet);
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
    static class IconClickHandler<T> implements EventHandler {

        private ObjectProperty<AnchorPane> os;

        private Set<AnchorPane> selectedSet;

        public IconClickHandler(ObjectProperty<AnchorPane> os,Set<AnchorPane> selectedSet) {
            if (os == null) {
                throw new RuntimeException("os cannot be null!");
            }
            this.os = os;
            this.selectedSet = selectedSet;
        }

        @Override
        public void handle(Event event) {
            AnchorPane curOs = (AnchorPane)event.getSource();
            AnchorPane lastOs = os.get();
            if (lastOs != null) {
                lastOs.setStyle("-fx-background-color: none");
            }
            curOs.setStyle("-fx-background-color: rgba(104,202,249,0.4)");
            os.set(curOs);
        }

        public AnchorPane getOs() {
            return os.get();
        }

        public ObjectProperty<AnchorPane> osProperty() {
            return os;
        }
    }
}
