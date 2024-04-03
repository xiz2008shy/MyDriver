package com.tom.handler.address;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class IconBakChangeHandler<T extends Node> implements EventHandler<MouseEvent> {

    private File file;

    private boolean flag = true;

    public IconBakChangeHandler(File file) {
        this.file = file;
    }

    @Override
    public void handle(MouseEvent event) {
        Node source = (T)event.getSource();
        if (flag){
            source.setStyle("-fx-background-color: #dce9f1");
        }else {
            source.setStyle("-fx-background-color: none");
        }
        this.flag = !flag;
    }
}
