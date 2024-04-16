package com.tom;

import com.tom.component.menu.RightClickMenu;
import com.tom.component.setting.MySetting;
import com.tom.controller.MyDriverPaneController;
import com.tom.general.RecWindows;
import com.tom.utils.ImageUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class FXMLVersionApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MySetting.initSetting(this.getParameters());
        File baseFile = new File(MySetting.getConfig().getBasePath());
        MyDriverPaneController myDriverPane = new MyDriverPaneController(baseFile);

        RecWindows recWindowsPane = new RecWindows(myDriverPane, 900.0,
                600.0, 12.0, stage,myDriverPane.getModelData());
        recWindowsPane.setWhenActive(RightClickMenu::addMenu);
        recWindowsPane.initStage();
        stage.getIcons().add(ImageUtils.getImageFromResources("/img/fileDir32.png",32,32));
        stage.show();
    }
}
