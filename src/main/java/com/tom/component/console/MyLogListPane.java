package com.tom.component.console;

import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;


public class MyLogListPane extends ScrollPane {

    private static final ListView<String> listView = new ListView<>();

    public static ListView<String> getListView(){
        return listView;
    }

    public MyLogListPane() {
        super();
        this.setContent(listView);
        this.setFitToWidth(true);
        this.setPrefHeight(100);
        listView.setStyle("-fx-background-insets: 0;-fx-background-color: white;");
        this.setStyle("-fx-padding: 0;-fx-background-insets: 0;-fx-background-color: white;");
    }
}
