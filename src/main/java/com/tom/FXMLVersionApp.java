package com.tom;

import com.tom.component.setting.MySetting;
import com.tom.controller.MyDriverPaneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class FXMLVersionApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MySetting.initSetting(this.getParameters());
        File baseFile = new File(MySetting.getConfig().getBasePath());
        MyDriverPaneController controller = new MyDriverPaneController(baseFile);

        Scene scene = new Scene(controller,1000,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
