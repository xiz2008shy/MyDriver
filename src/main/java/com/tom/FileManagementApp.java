package com.tom;

import com.tom.component.center.MyDriverPane;
import com.tom.general.RecWindows;
import com.tom.utils.ImageUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class FileManagementApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private final String curPath = "C:\\Users\\TOMQI\\Desktop";

    @Override
    public void start(Stage stage) {

        MyDriverPane myDriverPane = MyDriverPane.createMyDriverPane(curPath);

        RecWindows recWindowsPane = new RecWindows(myDriverPane, 900.0,
                600.0, 12.0, stage);
        recWindowsPane.setWhenActive(MyDriverPane::addMenu);
        recWindowsPane.initStage();


       /* MyDriverPane myDriverPane2 = createMyDriverPane();
        recWindowsPane.createNewTab(myDriverPane2,true);*/

        // 增加图标
        stage.getIcons().add(ImageUtils.getImageFromResources("/img/fileDir32.png",32,32));
        stage.show();
    }



}
