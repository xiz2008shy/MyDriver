package com.tom;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        ArcTo arcTo = new ArcTo();
        arcTo.setX(200);
        arcTo.setY(0);
        arcTo.setSweepFlag(true);
        arcTo.setRadiusX(-100);
        arcTo.setRadiusY(-50);


        Path path = new Path(new MoveTo(0, 0),
                new VLineTo(100),
               new HLineTo(100),
                new VLineTo(50),
                arcTo
        );
        HBox box = new HBox(path);

        box.setSpacing(10);

        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.show();
    }
}