package com.tom;

import com.tom.component.setting.MySetting;
import com.tom.controller.MyTabController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class FxmlTestMain extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        MySetting.initSetting(getParameters());
        FXMLLoader loader = new FXMLLoader();
        URL resource = getClass().getResource("/fxml/MyDriverPane.fxml");
        loader.setLocation(resource);
        AnchorPane root = loader.load();
        Pane pane = new Pane(root);


        Scene scene = new Scene(pane,800,600);
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.show();
    }
}