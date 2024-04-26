package com.tom.component.menu;

import com.tom.general.RecWindows;
import com.tom.general.StatusBar;
import com.tom.general.menu.BaseMenu;
import com.tom.general.menu.MenuShowFixed;
import com.tom.general.menu.MyMenuContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class StatusBarMenu {

    public static BaseMenu createBaseMenu(RecWindows windows) {
        BaseMenu baseMenu = new BaseMenu(160, 40, windows, MenuShowFixed.class);

        MyMenuContext open = new MyMenuContext(new Label("开启同步"), baseMenu);
        open.whenActiveByMouse(_ -> {
            System.out.println("开启同步");
        });

        MyMenuContext menu0 = new MyMenuContext(new Label("关闭同步"), baseMenu);
        menu0.whenActiveByMouse(_ -> {
            System.out.println("关闭同步");
        });
        windows.setStatusMenu(baseMenu);
        return baseMenu;
    }

    public static void addMenuTrigger(StatusBar statusBar) {
        BaseMenu baseMenu = createBaseMenu(statusBar.getWindows());
        statusBar.addEventHandler(MouseEvent.MOUSE_CLICKED,e -> {
            if (MouseButton.PRIMARY.equals(e.getButton())) {
                if (baseMenu.isShow()){
                    baseMenu.closeMenu(e);
                }else {
                    baseMenu.showMenu(e);
                }
            }
        });

    }
}




