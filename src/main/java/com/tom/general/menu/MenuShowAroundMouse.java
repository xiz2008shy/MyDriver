package com.tom.general.menu;

import com.tom.general.RecWindows;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MenuShowAroundMouse implements ShowMenu<MouseEvent>{

    private double myTransX = 0;
    private double myTransY = 0;
    private double menuWidth;
    private double menuHeight;
    private int intHeight;
    private int intWidth;


    @Override
    public void showMenu(MouseEvent event,BaseMenu baseMenu){
        showMenuAroundMouse(event,baseMenu);
    }

    @Override
    public void closeMenu(MouseEvent event, BaseMenu baseMenu) {
        HBox realPane = baseMenu.getRealPane();
        realPane.setTranslateX(-myTransX);
        realPane.setTranslateY(-myTransY);
        this.myTransX = 0 ;
        this.myTransY = 0 ;
        realPane.setVisible(false);
        if (baseMenu.getCloseMenuHandler() != null){
            baseMenu.getCloseMenuHandler().accept(baseMenu);
        }
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


    @Override
    public void setMenuWidth(double menuWidth) {
        this.menuWidth = menuWidth;
        this.intWidth = (int) menuWidth;
    }

    @Override
    public void setMenuHeight(double menuHeight) {
        this.menuHeight = menuHeight;
        this.intHeight = (int) menuHeight;
    }

}
