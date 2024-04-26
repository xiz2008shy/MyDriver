package com.tom.general.menu;

import com.tom.general.RecWindows;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MenuShowFixed implements ShowMenu<MouseEvent>{

    private int menuWidth;
    private int menuHeight;

    private boolean needCal = true;
    @Override
    public void showMenu(MouseEvent event, BaseMenu baseMenu) {
        System.out.println("x-" +event.getSceneX() +",y-" +event.getSceneY());
        RecWindows menuBindWindow = baseMenu.getMenuBindWindow();
        HBox realPane = baseMenu.getRealPane();
        if (needCal) {
            int halfWindowWidth = (int)menuBindWindow.getWidth() >> 1 ;
            int halfWindowHeight = (int)menuBindWindow.getHeight() >> 1;
            realPane.setTranslateX(halfWindowWidth - 145);
            realPane.setTranslateY(-halfWindowHeight + 80);
        }

        realPane.setVisible(true);
    }

    @Override
    public void closeMenu(MouseEvent event, BaseMenu baseMenu) {
        //System.out.println("x-" +event.getSceneX() +",y-" +event.getSceneY());
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
