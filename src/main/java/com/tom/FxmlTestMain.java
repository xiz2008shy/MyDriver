package com.tom;

import com.tom.controller.MyTabController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
        FXMLLoader loader = new FXMLLoader();
        URL resource = getClass().getResource("/fxml/MyTab.fxml");
        loader.setLocation(resource);
        AnchorPane root = loader.load();

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(resource);
        AnchorPane root2 = loader2.load();
        MyTabController myTabC2 = loader2.getController();
        myTabC2.setTitle("title2");

        VBox pane = new VBox(root, root2);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.show();
    }
}