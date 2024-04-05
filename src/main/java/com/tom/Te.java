package com.tom;

import com.tom.pane.RecWindows;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Te extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Button button = new Button("cea");
        RecWindows recWindows = new RecWindows(button, 800, 600,10, primaryStage);
        recWindows.initStage();
        primaryStage.show();

    }
}
