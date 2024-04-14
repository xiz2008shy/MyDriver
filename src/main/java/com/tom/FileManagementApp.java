package com.tom;

import com.tom.component.center.MyDriverPane;
import com.tom.component.menu.RightClickMenu;
import com.tom.component.setting.MySetting;
import com.tom.general.RecWindows;
import com.tom.utils.ImageUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class FileManagementApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage stage) {

        MySetting.initSetting( getParameters());
        MyDriverPane myDriverPane = MyDriverPane.createMyDriverPane(MySetting.getConfig().getBasePath());
        RecWindows recWindowsPane = new RecWindows(myDriverPane, 900.0,
                600.0, 12.0, stage);
        recWindowsPane.setWhenActive(RightClickMenu::addMenu);
        recWindowsPane.initStage();
        stage.getIcons().add(ImageUtils.getImageFromResources("/img/fileDir32.png",32,32));
        stage.show();

    }

}
