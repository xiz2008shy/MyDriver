package com.tom.component.menu;

import com.tom.general.RecWindows;
import com.tom.general.StatusBar;
import com.tom.general.menu.BaseMenu;
import com.tom.general.menu.MenuShowFixed;
import com.tom.general.menu.MyMenuContext;
import com.tom.job.SyncJob;
import com.tom.utils.ImageUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


public class StatusBarMenu {

    public static BaseMenu createBaseMenu(RecWindows windows) {
        BaseMenu baseMenu = new BaseMenu(180, 40, windows, MenuShowFixed.class);
        StatusBar statusBar = windows.getTopBar().getStatusBar();

        ImageView connectionIcon = ImageUtils.getImageView("/img/connection.png", 16, 16);
        HBox.setMargin(connectionIcon,new Insets(0,15,0,0));
        MyMenuContext autoConnect = new MyMenuContext(baseMenu,connectionIcon,new Label("开启自动同步"));
        autoConnect.whenActiveByMouse(_ -> {
            statusBar.switchWaitingIcon();
            Thread.startVirtualThread(statusBar::switchOnline);
        });
        autoConnect.setDisabledPredicate(_ -> statusBar.isOnline());

        ImageView disconnectionIcon = ImageUtils.getImageView("/img/disconnection.png", 16, 16);
        HBox.setMargin(disconnectionIcon,new Insets(0,15,0,0));
        MyMenuContext closeAutoConn = new MyMenuContext(baseMenu,disconnectionIcon,new Label("关闭自动同步"));
        closeAutoConn.whenActiveByMouse(_ -> {
            statusBar.switchWaitingIcon();
            statusBar.switchOffline();
        });
        closeAutoConn.setDisabledPredicate(_ -> !statusBar.isOnline());

        ImageView syncRightNow = ImageUtils.getImageView("/img/syncIcon.png", 16, 16);
        HBox.setMargin(syncRightNow,new Insets(0,15,0,0));
        MyMenuContext menu1 = new MyMenuContext(baseMenu,syncRightNow,new Label("立即同步"));
        menu1.whenActiveByMouse(_ -> {
            SyncJob.start(windows.getStage(),statusBar);
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




