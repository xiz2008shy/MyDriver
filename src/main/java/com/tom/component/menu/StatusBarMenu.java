package com.tom.component.menu;

import com.tom.controller.MyDriverPaneController;
import com.tom.general.RecWindows;
import com.tom.general.StatusBar;
import com.tom.general.menu.BaseMenu;
import com.tom.general.menu.MyMenuContext;
import com.tom.handler.fxml.DesktopIconClickHandler;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.List;

public class StatusBarMenu {

    public static BaseMenu createBaseMenu(RecWindows windows) {
        BaseMenu baseMenu = new BaseMenu(160, 40, windows);

        MyMenuContext open = new MyMenuContext(new Label("开启同步"), baseMenu);
        open.whenActiveByMouse(_ -> {
            System.out.println("开启同步");
        });

        MyMenuContext menu0 = new MyMenuContext(new Label("关闭同步"), baseMenu);
        menu0.whenActiveByMouse(_ -> {
            System.out.println("关闭同步");
        });

        return baseMenu;
    }

    public static void addMenuTrigger(StatusBar statusBar) {
        BaseMenu baseMenu = createBaseMenu(statusBar.getWindows());
        statusBar.addEventHandler(MouseEvent.MOUSE_CLICKED,e -> {
            baseMenu.showMenu(e,statusBar.getWindows());
        });

    }
}




