package com.tom;

import com.tom.component.center.MainFlowContentPart;
import com.tom.component.center.MainScrollPart;
import com.tom.component.top.AddressTab;
import com.tom.model.AddressProperty;
import com.tom.pane.RecWindows;
import com.tom.utils.ImageUtils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileManagementApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private final String curPath = "C:\\Users\\TOMQI\\Desktop";

    @Override
    public void start(Stage stage) {

        AddressProperty addressProperty = new AddressProperty(curPath);
        // 最内层的流布局
        MainFlowContentPart mainFlowContentPart = new MainFlowContentPart(addressProperty);

        // 中层的滚动布局
        MainScrollPart mainScrollPart = new MainScrollPart(mainFlowContentPart);
        ScrollPane scrollPane = mainScrollPart.getScrollPane();

        // 地址栏组件
        AddressTab topPart = new AddressTab(addressProperty,mainFlowContentPart);

        // 最外层的方位布局组件
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topPart.getAddressPane());
        borderPane.setCenter(scrollPane);

        BorderPane.setMargin(scrollPane,new Insets(0,5,0,20));

        RecWindows recWindowsPane = new RecWindows(borderPane, 800.0, 600.0, 20.0);
        // scene
        recWindowsPane.initStage(stage);
        recWindowsPane.setTitle("MyDriver");

        // 增加图标
        stage.getIcons().add(ImageUtils.getImageFromResources("/img/fileDir32.png",32,32));

        stage.show();
    }

}
