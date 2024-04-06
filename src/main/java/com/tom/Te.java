package com.tom;

import com.tom.pane.HeadTab;
import com.tom.pane.RecWindows;
import com.tom.utils.AnchorPaneUtil;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


public class Te extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {


        VBox vb = new VBox();
        vb.setPrefSize(260,35);
        vb.setStyle("-fx-background-color: red");
        Shape shape = HeadTab.headTabSharp(260, 35, 7);
        vb.setShape(shape);
        AnchorPane ap = new AnchorPane(vb);
        AnchorPaneUtil.setNode(vb,100.0,null,null,100.0);

        Button button = new Button("sda");
        ap.getChildren().add(button);
        ap.setStyle("-fx-background-color: black");
        AnchorPaneUtil.setNode(button,300.0,null,null,100.0);

        RecWindows recWindows = new RecWindows(ap, 800, 600,10, primaryStage,new SimpleStringProperty("TEST"));
        recWindows.initStage();
        primaryStage.show();

    }
}
