package com.tom.pane;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
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

import java.io.File;

public class TopBar<T> {

    public static final String ACTIVE_STYLE = "-fx-background-color: rgb(246, 243, 243)";
    public static final String INACTIVE_STYLE = "-fx-background-color: rgb(216, 218, 219)";
    public static final String MOVE_ON_STYLE = "-fx-background-color: rgb(213, 208, 206)";
    public static final Shape ACTIVE_SHARP = HeadTab.headTabSharp(260, 35, 7);
    public static final Shape INACTIVE_SHARP = HeadTab.headTabSecSharp(260, 35, 7);
    private final AnchorPane topBar;

    private RecWindows recWindows;

    private HBox closeBox;

    private HBox maximizeBox;

    private HBox minimizeBox;

    private HBox leftTabs;

    private IntegerProperty activeProperty = new SimpleIntegerProperty(0);

    public TopBar(RecWindows recWindows, ObjectProperty<T> obj) {
        this.recWindows = recWindows;
        this.topBar = new AnchorPane();
        topBar.setPrefHeight(25);
        topBar.setStyle("-fx-background-color: rgb(216, 218, 219)");
        HBox rightIcons = createRightPart(recWindows);
        this.leftTabs = new HBox();
        createTab(obj,true);
        createTab(obj,false);
        topBar.getChildren().addAll(leftTabs,rightIcons);
        AnchorPaneUtil.setNode(leftTabs,7.0,160.0,0.0, 0.0);
        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);
    }


    public void createTab(ObjectProperty<T> title,boolean isActive) {
        AnchorPane ap = new AnchorPane();

        if (isActive){
            ap.setShape(ACTIVE_SHARP);
            ap.setStyle(ACTIVE_STYLE);
        }else {
            ap.setShape(INACTIVE_SHARP);
            ap.setStyle(INACTIVE_STYLE);
        }


        ap.setPrefSize(260,35);
        ImageView imageView = ImageUtils.getImageView("/img/fileDir32.png", 19, 19);
        Label label = new Label();
        File file = (File)title.get();
        label.setText(file.getName());
        title.addListener((_,  _, newValue) -> {
            File lF = (File)newValue;
            label.setText(lF.getName());
        });
        label.setStyle("-fx-text-overrun: ellipsis");
        HBox textBox = new HBox(label);
        textBox.setAlignment(Pos.CENTER_LEFT);
        ap.getChildren().addAll(imageView,textBox);

        AnchorPaneUtil.setNode(imageView,5.0,null,0.0, 15.0);
        AnchorPaneUtil.setNode(textBox,0.0,15.0,0.0, 45.0);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        int size = leftTabs.getChildren().size();
        IntegerProperty curIndex = new SimpleIntegerProperty(size);
        if (isActive){
            this.activeProperty.set(curIndex.get());
        }

        if(size != 0){
            HBox.setMargin(ap,new Insets(0,0,0,-7));
        }

        ap.addEventHandler(MouseEvent.MOUSE_ENTERED,_ -> {
            if (curIndex.get() != activeProperty.get()) {
                ap.setStyle(MOVE_ON_STYLE);
            }
        });
        ap.addEventHandler(MouseEvent.MOUSE_EXITED,_ -> {
            if (curIndex.get() != activeProperty.get()) {
                ap.setStyle(INACTIVE_STYLE);
            }
        });
        ap.addEventHandler(MouseEvent.MOUSE_CLICKED,_ -> {
            if (curIndex.get() != activeProperty.get()) {
                activeProperty.set(curIndex.get());
            }
        });
        activeProperty.addListener((_,  _, newValue) -> {
            if ( (int)newValue == curIndex.get()){
                ap.setShape(ACTIVE_SHARP);
                ap.setStyle(ACTIVE_STYLE);
            }else {
                ap.setShape(INACTIVE_SHARP);
                ap.setStyle(INACTIVE_STYLE);
            }
        });
        leftTabs.getChildren().add(ap);
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
