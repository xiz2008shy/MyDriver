package com.tom;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class StartApplication extends Application {

    public static void main(String[] args) {
        Application.launch(StartApplication.class,args);
    }


    public void start(Stage stage) throws Exception {
        HostServices hostServices = getHostServices();
        hostServices.showDocument("www.baidu.com");
        stage.setTitle("fx learning");
        //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("vector-marketing-icon.jpg");
        URL url = this.getClass().getClassLoader().getResource("vector-marketing-icon.jpg");
        //stage.getIcons().add(new Image(inputStream));
        Button button1 = new Button("button1");
        button1.setPrefWidth(200);
        button1.setPrefHeight(200);
        Button button2 = new Button("button2");
        button2.setPrefWidth(200);
        button2.setPrefHeight(200);
        Button button3 = new Button("button3");
        button3.setPrefWidth(200);
        button3.setPrefHeight(200);
        Group group = new Group();
        group.getChildren().addAll(button1,button2,button3);
        System.out.println(group.contains(0,10));
        Scene scene = new Scene(group);
        scene.setCursor(Cursor.cursor(url.toExternalForm()));
        stage.setScene(scene);
        stage.show();
        System.out.println("start method doing");
    }
}