package com.tom.client;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClientApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Tom Client");
        Screen primaryS = Screen.getPrimary();
        Rectangle2D visualBounds = primaryS.getVisualBounds();
        double maxWidth = visualBounds.getWidth();
        double maxHeight = visualBounds.getHeight();
        stage.setWidth(maxWidth*0.7);
        stage.setHeight(maxHeight*0.7);
        stage.setScene(new Scene(createContent()));
        stage.show();
    }


    public Parent createContent(){
        Rectangle rectangle = new Rectangle(100, 60, Color.BISQUE);
        rectangle.setTranslateX(10);
        rectangle.setTranslateY(10);
        return new Pane(rectangle);
    }
}
