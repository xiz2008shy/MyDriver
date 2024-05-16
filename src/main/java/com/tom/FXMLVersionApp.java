package com.tom;

import com.tom.component.menu.RightClickMenu;
import com.tom.component.menu.StatusBarMenu;
import com.tom.config.MySetting;
import com.tom.controller.MyDriverPaneController;
import com.tom.general.RecWindows;
import com.tom.utils.ImageUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class FXMLVersionApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MySetting.initSetting(this.getParameters());
        File baseFile = new File(MySetting.getConfig().getBasePath());
        MyDriverPaneController myDriverPane = new MyDriverPaneController(baseFile);

        // topBarIconFlag 15 = 8 | 4 | 2 | 1
        RecWindows mainWindow = new RecWindows(myDriverPane, 1200,
                700, 12.0, stage,myDriverPane.getModelData(),31);
        mainWindow.setWhenActive(RightClickMenu::addMenu);
        StatusBarMenu.addMenuTrigger(mainWindow.getTopBar().getStatusBar());
        mainWindow.initStage();
        mainWindow.setKeyHandler();
        MySetting.setMainWindows(mainWindow);
        stage.getIcons().add(ImageUtils.getImageFromResources("/img/fileDir32.png",32,32));
        stage.show();


    }
}
