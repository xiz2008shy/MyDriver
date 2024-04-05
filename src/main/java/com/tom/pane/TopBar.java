package com.tom.pane;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TopBar {

    private final AnchorPane topBar;

    private String title;

    private RecWindows recWindows;

    private HBox closeBox;

    private HBox maximizeBox;

    private HBox minimizeBox;

    public TopBar(RecWindows recWindows) {
        this.recWindows = recWindows;
        this.topBar = new AnchorPane();
        topBar.setPrefHeight(25);
        topBar.setStyle("-fx-background-color: rgb(216, 218, 219)");
        FlowPane rightIcons = new FlowPane();
        rightIcons.setPrefWidth(152);

        EventHandler<MouseEvent> blueMoveOnHandler = getBlueOrRedMoveOnHandler(true,null,null);
        HBox h1 = createSquareIconBox("/img/gear.png", 32,20, 9,blueMoveOnHandler);

        this.minimizeBox = createSquareIconBox("/img/minimize.png", 32,14, 12,blueMoveOnHandler);
        minimizeBox.setOnMouseClicked(recWindows.minimizedHandler());

        this.maximizeBox = createSquareIconBox("/img/maximize.png", 16,14, 12,blueMoveOnHandler);
        maximizeBox.setOnMouseClicked(recWindows.maximizedHandler());

        this.closeBox = createSquareIconBox("/img/close.png", 16,14, 12,null);

        ImageView normal = (ImageView)closeBox.getChildren().getFirst();
        ImageView closed = ImageUtils.getImageView("/img/close-white.png",16,14);
        EventHandler<MouseEvent> redMoveOnHandler = getBlueOrRedMoveOnHandler(false,normal,closed);
        setMoveOnHandler(redMoveOnHandler, closeBox);
        closeBox.setOnMouseClicked(_ -> Platform.exit());

        rightIcons.getChildren().addAll(h1,minimizeBox,maximizeBox,closeBox);
        topBar.getChildren().addAll(rightIcons);
        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);
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

    public void setTitle(String title) {
        this.title = title;
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
