package com.tom.pane;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TopBar<T> {


    private final AnchorPane topBar = new AnchorPane();
    private final TabManager tabManager;

    private HBox closeBox;

    private HBox maximizeBox;

    private HBox minimizeBox;


    public TopBar(RecWindows recWindows, TabWatcher<T> tabWatcher) {
        this.tabManager = new TabManager(recWindows);
        tabManager.createTab(tabWatcher,true,true);
        initTopBar(recWindows);
    }


    public TopBar(RecWindows recWindows, Node node ,TabWatcher<T> tabWatcher) {
        this.tabManager = new TabManager(recWindows);
        tabManager.createTab(node,tabWatcher,true,true);
        initTopBar(recWindows);
    }


    private void initTopBar(RecWindows recWindows) {
        topBar.setPrefHeight(25);
        topBar.setStyle("-fx-background-color: rgb(216, 218, 219)");
        HBox rightIcons = createRightPart(recWindows);
        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);
        topBar.getChildren().addAll(tabManager.getTabs(),rightIcons);
        AnchorPaneUtil.setNode(tabManager.getTabs(),7.0,160.0,0.0, 0.0);
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


    public <R extends Pane>EventHandler<MouseEvent> getBlueOrRedMoveOnHandler(boolean blueOrRed,ImageView normal,ImageView closed) {
        final boolean[] flag = {true};
        final String colorStyle = blueOrRed ? "-fx-background-color: #dce9f1" : "-fx-background-color: rgb(196, 43, 28)";
        return  e -> {
            Pane source = (R)e.getSource();
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

    public TabManager getTabManager() {
        return tabManager;
    }
}
