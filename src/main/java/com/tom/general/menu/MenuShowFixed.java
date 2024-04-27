package com.tom.general.menu;

import com.tom.general.RecWindows;
import com.tom.general.StatusBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class MenuShowFixed implements ShowMenu<MouseEvent>{

    private int menuWidth;
    private int menuHeight;

    private boolean needCal = true;
    @Override
    public void showMenu(MouseEvent event, BaseMenu baseMenu) {
        event.consume();
        RecWindows windows = baseMenu.getMenuBindWindow();
        if (windows.getBaseMenu().isShow()){
            windows.getBaseMenu().closeMenu(null);
        }
        StatusBar statusBar = windows.getTopBar().getStatusBar();
        statusBar.getStyleClass().add("my_status_bar_active");
        HBox realPane = baseMenu.getRealPane();
        if (needCal) {
            int halfWindowWidth = (int)windows.getWidth() >> 1;
            int halfWindowHeight = (int)windows.getHeight() >> 1;
            realPane.setTranslateX(halfWindowWidth - 145);
            realPane.setTranslateY(-halfWindowHeight + 80);
        }
        //baseMenu.setMenuBg(windows.getShowBox(),event.getSceneX(),event.getSceneY());
        realPane.setVisible(true);
    }

    @Override
    public void closeMenu(MouseEvent event, BaseMenu baseMenu) {
        if (event != null){
            event.consume();
        }
        RecWindows menuBindWindow = baseMenu.getMenuBindWindow();
        StatusBar statusBar = menuBindWindow.getTopBar().getStatusBar();
        statusBar.getStyleClass().remove("my_status_bar_active");
        baseMenu.getRealPane().setVisible(false);
    }

    @Override
    public void setMenuWidth(double menuWidth) {
        this.menuWidth = (int)menuWidth;
    }

    @Override
    public void setMenuHeight(double menuHeight) {
        this.menuHeight = (int)menuHeight;
    }
}
