package com.tom.pane;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Shape;

public class TopBar {

    private final AnchorPane topBar;

    private StringProperty title;

    private RecWindows recWindows;

    private HBox closeBox;

    private HBox maximizeBox;

    private HBox minimizeBox;

    public TopBar(RecWindows recWindows,StringProperty title) {
        this.recWindows = recWindows;
        this.topBar = new AnchorPane();
        this.title = title;
        topBar.setPrefHeight(25);
        topBar.setStyle("-fx-background-color: rgb(216, 218, 219)");
        HBox rightIcons = createRightPart(recWindows);
        HBox leftPart = createLeftPart(recWindows,title);
        topBar.getChildren().addAll(leftPart,rightIcons);
        AnchorPaneUtil.setNode(leftPart,7.0,160.0,0.0, 0.0);
        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);
    }

    private HBox createLeftPart(RecWindows recWindows,StringProperty title) {
        HBox leftTabs = new HBox();

        Pane ap1 = createTab(title,true);
        Pane ap2 = createTab(title,false);

        leftTabs.getChildren().addAll(ap1,ap2);

        return leftTabs;
    }

    private static Pane createTab(StringProperty title,boolean isActive) {
        AnchorPane ap = new AnchorPane();
        Shape shape ;
        if (isActive){
            shape = HeadTab.headTabSharp(260, 35, 7);
            ap.setStyle("-fx-background-color: rgb(246, 243, 243)");
        }else {
            shape = HeadTab.headTabSecSharp(260, 35, 7);
            ap.setStyle("-fx-background-color: rgb(216, 218, 219)");
            HBox.setMargin(ap,new Insets(0,0,0,-7));
        }

        ap.setShape(shape);
        ap.setPrefSize(260,35);
        ImageView imageView = ImageUtils.getImageView("/img/fileDir32.png", 19, 19);
        Label label = new Label();
        label.textProperty().bind(title);
        label.setStyle("-fx-text-overrun: ellipsis");
        HBox textBox = new HBox(label);
        textBox.setAlignment(Pos.CENTER_LEFT);
        ap.getChildren().addAll(imageView,textBox);

        AnchorPaneUtil.setNode(imageView,5.0,null,0.0, 15.0);
        AnchorPaneUtil.setNode(textBox,0.0,15.0,0.0, 45.0);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        return ap;
    }


    private HBox createRightPart(RecWindows recWindows) {
        HBox rightIcons = new HBox();
        rightIcons.setPrefWidth(152);

        EventHandler<MouseEvent> blueMoveOnHandler = getBlueOrRedMoveOnHandler(true,null,null);
        HBox h1 = createSquareIconBox("/img/gear.png", 32,20, 9,blueMoveOnHandler);

        this.minimizeBox = createSquareIconBox("/img/minimize.png", 32,14, 12,blueMoveOnHandler);
        EventHandler<MouseEvent> minimizedHandler = recWindows.minimizedHandler(minimizeBox);
        minimizeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,minimizedHandler);
        minimizeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,minimizedHandler);

        this.maximizeBox = createSquareIconBox("/img/maximize.png", 16,14, 12,blueMoveOnHandler);
        EventHandler<MouseEvent> maximizedHandler = recWindows.maximizedHandler(maximizeBox);
        maximizeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,maximizedHandler);
        maximizeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,maximizedHandler);

        this.closeBox = createSquareIconBox("/img/close.png", 16,14, 12,null);

        ImageView normal = (ImageView)closeBox.getChildren().getFirst();
        ImageView closed = ImageUtils.getImageView("/img/close-white.png",16,14);
        EventHandler<MouseEvent> redMoveOnHandler = getBlueOrRedMoveOnHandler(false,normal,closed);
        setMoveOnHandler(redMoveOnHandler, closeBox);
        EventHandler<MouseEvent> closeHandler = recWindows.closeHandler(closeBox);
        closeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,closeHandler);
        closeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,closeHandler);

        rightIcons.getChildren().addAll(h1,minimizeBox,maximizeBox,closeBox);
        return rightIcons;
    }

    private static HBox createSquareIconBox(String image, int size, int resize,int padding,EventHandler<MouseEvent> moveOnHandler) {
        ImageView closeIcon = ImageUtils.getImageView(image, size, resize);
        HBox hbox = new HBox(closeIcon);
        hbox.setPadding(new Insets(padding));
        setMoveOnHandler(moveOnHandler, hbox);
        return hbox;
    }

    private static void setMoveOnHandler(EventHandler<MouseEvent> moveOnHandler, HBox hbox) {
        if (moveOnHandler != null) {
            hbox.setOnMouseEntered(moveOnHandler);
            hbox.setOnMouseExited(moveOnHandler);
        }
    }


    public <T extends Pane>EventHandler<MouseEvent> getBlueOrRedMoveOnHandler(boolean blueOrRed,ImageView normal,ImageView closed) {
        final boolean[] flag = {true};
        final String colorStyle = blueOrRed ? "-fx-background-color: #dce9f1" : "-fx-background-color: rgb(196, 43, 28)";
        return  e -> {
            Pane source = (T)e.getSource();
            if (flag[0]) {
                source.setStyle(colorStyle);
                if (!blueOrRed){
                    ObservableList<Node> children = source.getChildren();
                    children.clear();
                    children.add(closed);
                }
            } else {
                source.setStyle("-fx-background-color: none");
                if (!blueOrRed){
                    ObservableList<Node> children = source.getChildren();
                    children.clear();
                    children.add(normal);
                }
            }
            flag[0] = !flag[0];
        };
    }

    public AnchorPane getTopBar() {
        return topBar;
    }

    public HBox getCloseBox() {
        return closeBox;
    }

    public HBox getMaximizeBox() {
        return maximizeBox;
    }
}
