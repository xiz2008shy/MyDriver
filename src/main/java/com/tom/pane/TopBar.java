package com.tom.pane;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class TopBar {

    private final AnchorPane topBar;

    private String title;

    public TopBar() {
        this.topBar = new AnchorPane();
        topBar.setPrefHeight(25);
        topBar.setStyle("-fx-background-color: rgb(216, 218, 219)");
        FlowPane rightIcons = new FlowPane();
        rightIcons.setPrefWidth(152);

        HBox h1 = createSquareIconBox("/img/gear.png", 32,20, 9);

        HBox h2 = createSquareIconBox("/img/minimize.png", 32,14, 12);

        HBox h3 = createSquareIconBox("/img/maximize.png", 16,14, 12);

        HBox h4 = createSquareIconBox("/img/close.png", 32,14, 12);

        rightIcons.getChildren().addAll(h1,h2,h3,h4);
        topBar.getChildren().addAll(rightIcons);
        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);

    }

    private static HBox createSquareIconBox(String image, int size, int resize,int padding) {
        ImageView closeIcon = ImageUtils.getImageViewFromResources(image,size,size);
        ImageUtils.resize(closeIcon, resize, resize);
        HBox hbox = new HBox(closeIcon);
        hbox.setPadding(new Insets(padding));
        return hbox;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public AnchorPane getTopBar() {
        return topBar;
    }
}
