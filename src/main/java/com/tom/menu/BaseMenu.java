package com.tom.menu;

import com.tom.general.RecWindows;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class BaseMenu extends StackPane{

    private final ImageView bg;

    private final VBox menuContent;

    private final double myWidth;
    private final int intWidth;

    private double preHeight;
    private double myHeight;
    private int intHeight;

    private Rectangle rectangle;

    private final HBox realPane;

    private double myTransX = 0;
    private double myTransY = 0;


    public BaseMenu(double width,double preHeight, RecWindows windows) {
        super();
        this.myWidth = width;
        this.myHeight = preHeight;
        this.preHeight = preHeight;
        this.intWidth = (int)width;
        this.intHeight = (int)preHeight;

        this.rectangle = new Rectangle(width, myHeight);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        myResize(width,myHeight);

        this.realPane = new HBox();
        realPane.getChildren().add(this);
        DropShadow dropShadow = new DropShadow();
        // 设置阴影的模糊半径
        dropShadow.setRadius(5.0);
        // 设置阴影的水平偏移
        dropShadow.setOffsetX(3.0);
        // 设置阴影的垂直偏移
        dropShadow.setOffsetY(3.0);
        // 设置阴影的颜色
        dropShadow.setColor(Color.GRAY);
        // 设置阴影的宽度（即阴影的尺寸）
        dropShadow.setWidth(10.0);
        //dropShadow.setSpread(0.5);
        realPane.setEffect(dropShadow);

        this.bg = new ImageView();
        bg.setEffect(new GaussianBlur(20));
        this.menuContent = new VBox();
        menuContent.setPrefSize(width,myHeight);
        menuContent.setStyle("-fx-background-color: rgba(232,232,232,0.55);");
        this.getChildren().add(bg);
        this.getChildren().add(menuContent);
        windows.getSecPane().getChildren().add(this.realPane);
        this.realPane.setVisible(false);
    }

    private void myResize(double width,double height) {
        this.setPrefSize(width,myHeight);
        this.setMaxWidth(width);
        this.setMaxHeight(myHeight);
        this.setShape(rectangle);
        this.setClip(rectangle);
    }

    public void setMenuBg(Pane pane,double x,double y){
        WritableImage writableImage = new WritableImage(intWidth, intHeight);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(x,y,myWidth,myHeight));
        pane.snapshot(snapshotParameters,writableImage);
        bg.setImage(writableImage);
    }

    public void addMenuContent(MyMenuContext myMenuContext){
        ObservableList<Node> children = menuContent.getChildren();
        children.add(myMenuContext);
        VBox.setVgrow(myMenuContext, Priority.ALWAYS);
        this.myResize(myWidth, this.getChildren().size() * preHeight);
        myMenuContext.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            myMenuContext.getMouseActiveHandler().handle(e);
            this.closeMenu();
        });

    }


    public void showMenu( MouseEvent event,RecWindows pane){
        closeMenu();
        double transX = 0;
        double transY = 0;
        if (myTransX > event.getSceneX()) {
            transX = myTransX - event.getSceneX();
        }else {
            transX = event.getSceneX() - myTransX;
        }

        if (myTransY > event.getSceneY()) {
            transY = myTransY - event.getSceneY();
        }else {
            transY = event.getSceneY() - myTransY;
        }
        this.myTransX = event.getSceneX();
        this.myTransY = event.getSceneY();

        this.realPane.setTranslateX(transX);
        this.realPane.setTranslateY(transY);
        System.out.println(STR."myTranX-\{myTransX},myTranY-\{myTransY}");
        System.out.println(STR."tranX-\{transX},tranY-\{transY}");
        System.out.println(STR."realTransX-\{this.realPane.getTranslateX()},realTransY-\{this.realPane.getTranslateY()}");

        this.setMenuBg(pane.getShowBox(),event.getSceneX(),event.getSceneY());

        this.realPane.setVisible(true);
    }

    public void closeMenu(){
        this.realPane.setTranslateX(-myTransX);
        this.realPane.setTranslateY(-myTransY);
        this.myTransX = 0 ;
        this.myTransY = 0 ;
        this.realPane.setVisible(false);
    }


    public boolean isShow() {
        return this.realPane.isVisible();
    }
}
