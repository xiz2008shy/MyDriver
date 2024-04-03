package com.tom;

import com.tom.component.MainAddressPart;
import com.tom.component.MainFlowContentPart;
import com.tom.component.MainScrollPart;
import com.tom.handler.key.CopyHandler;
import com.tom.model.AddressProperty;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FileManagementApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private String curPath = "C:\\Users\\TOMQI\\Desktop";

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("MyDriver");
        AddressProperty addressProperty = new AddressProperty(curPath);
        // 最内层的流布局
        MainFlowContentPart mainFlowContentPart = new MainFlowContentPart(addressProperty);
        FlowPane flowPane = mainFlowContentPart.getFlowPane();
        // 中层的滚动布局
        MainScrollPart mainScrollPart = new MainScrollPart(flowPane);
        ScrollPane scrollPane = mainScrollPart.getScrollPane();
        // 地址栏组件
        MainAddressPart mainAddressPart = new MainAddressPart(addressProperty,mainFlowContentPart);
        AnchorPane addressPane = mainAddressPart.getAddressPane();

        // 最外层的方位布局组件
        BorderPane borderPane = new BorderPane();
        BorderPane.setMargin(flowPane,new Insets(0,10,0,10));
        borderPane.setCenter(scrollPane);
        borderPane.setTop(addressPane);


        scrollPane.addEventHandler(KeyEvent.KEY_PRESSED, new CopyHandler(mainFlowContentPart));

        // scene
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(700);
        stage.show();
    }

}
