package com.tom.general.menu;

import com.tom.general.RecWindows;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class MenuShowAroundMouse implements ShowMenu<MouseEvent>{

    private double myTransX = 0;
    private double myTransY = 0;
    private double menuWidth;
    private double menuHeight;
    private int intHeight;
    private int intWidth;

    public MenuShowAroundMouse(double menuWidth, double menuHeight) {
        this.menuWidth = menuWidth;
        this.menuHeight = menuHeight;

        this.intWidth = (int) menuWidth;
        this.intHeight = (int) menuHeight;
    }

    @Override
    public ShowMenu<MouseEvent> createShowMenu(double menuWidth, double menuHeight) {
        return new MenuShowAroundMouse(menuWidth,menuHeight);
    }

    @Override
    public void showMenu(MouseEvent event,BaseMenu baseMenu){
        showMenuAroundMouse(event,baseMenu);
    }



    private void showMenuAroundMouse(MouseEvent event, BaseMenu baseMenu){
        double originX ;
        double originY ;
        RecWindows window = baseMenu.getMenuBindWindow();
        double maxWWhI = window.getWidth() - menuWidth;
        double halfW = (int)window.getWidth() >> 1;
        double maxHWhI = window.getHeight() - menuHeight;
        double halfH = (int)window.getHeight() >> 1;
        int menuHalfWidth = intWidth >> 1;
        int menuHalfHeight = intHeight >> 1;
        /**
         * 这里根据当前鼠标点击位置坐标和原菜单面版的 宽度/高度比较 确定选取菜单面板四角中的一角坐标作为计算原点
         */
        if (event.getSceneX() < maxWWhI){
            if (event.getSceneY() < maxHWhI) {
                originX = halfW - menuHalfWidth;
                originY = halfH - menuHalfHeight;
            }else {
                originX = halfW - menuHalfWidth;
                originY = halfH + menuHalfHeight;
            }
        }else {
            if (event.getSceneY() < maxHWhI) {
                originX = halfW + menuHalfWidth;
                originY = halfH - menuHalfHeight;
            }else {
                originX = halfW + menuHalfWidth;
                originY = halfH + menuHalfHeight;
            }
        }


        myTransX =  event.getSceneX() - originX;
        myTransY =  event.getSceneY() - originY;

        HBox realPane = baseMenu.getRealPane();
        realPane.setTranslateX(myTransX);
        realPane.setTranslateY(myTransY);
        baseMenu.setMenuBg(window.getShowBox(),event.getSceneX(),event.getSceneY());
        for (Node child : baseMenu.getMenuContent().getChildren()) {
            MyMenuContext menuContext = (MyMenuContext) child;
            menuContext.getStyleClass().remove("my_disabled");
            if (menuContext.getVisiblePredicate().test(event)){
                menuContext.setVisible(true);
                if (menuContext.getDisabledPredicate().test(event)) {
                    menuContext.getStyleClass().add("my_disabled");
                    menuContext.setDisable(true);
                }else {
                    menuContext.setDisable(false);
                }
            }else {
                menuContext.setVisible(false);
            }
        }

        realPane.setVisible(true);
    }

}
