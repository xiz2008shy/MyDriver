package com.tom;

import com.tom.component.setting.MySetting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
        URL resource = getClass().getResource("/fxml/AddressPane.fxml");
        loader.setLocation(resource);
        AnchorPane root = loader.load();

        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.show();
    }
}