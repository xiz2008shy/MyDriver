package com.tom.component.console;

import javafx.scene.control.ListView;


public class MyLogListPane {

    private static final ListView<String> listView = new ListView<>();

    static {
        listView.setPrefHeight(100);
        listView.setStyle("-fx-background-insets: 0;-fx-background-color: white;");
    }

    public static void resize(double width,double height){
        listView.setPrefWidth(width);
        listView.setPrefHeight(height);
    }

    public static ListView<String> getListView(){
        return listView;
    }

}
