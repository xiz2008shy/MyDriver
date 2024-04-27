package com.tom.component.menu;

import com.tom.config.MySetting;
import com.tom.general.RecWindows;
import com.tom.general.StatusBar;
import com.tom.general.menu.BaseMenu;
import com.tom.general.menu.MenuShowFixed;
import com.tom.general.menu.MyMenuContext;
import com.tom.utils.ImageUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class StatusBarMenu {

    public static BaseMenu createBaseMenu(RecWindows windows) {
        BaseMenu baseMenu = new BaseMenu(160, 40, windows, MenuShowFixed.class);
        StatusBar statusBar = windows.getTopBar().getStatusBar();

        ImageView connectionIcon = ImageUtils.getImageView("/img/connection.png", 16, 16);
        HBox.setMargin(connectionIcon,new Insets(0,15,0,0));
        MyMenuContext open = new MyMenuContext(baseMenu,connectionIcon,new Label("开启同步"));
        open.whenActiveByMouse(_ -> {
            statusBar.switchOnline();

        });
        open.setDisabledPredicate(_ -> statusBar.isOnline() && MySetting.getRemoteSessionFactory() != null);

        ImageView disconnectionIcon = ImageUtils.getImageView("/img/disconnection.png", 16, 16);
        HBox.setMargin(disconnectionIcon,new Insets(0,15,0,0));
        MyMenuContext menu0 = new MyMenuContext(baseMenu,disconnectionIcon,new Label("关闭同步"));
        menu0.whenActiveByMouse(_ -> {
            statusBar.switchOffline();
        });
        menu0.setDisabledPredicate(_ -> !statusBar.isOnline() && MySetting.getRemoteSessionFactory() == null);
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




