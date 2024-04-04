package com.tom.component.top;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class TopBar {

    private final AnchorPane topBar;

    private AddressTab addressTab;

    private String title;

    public TopBar() {
        this.topBar = new AnchorPane();
        topBar.setPrefHeight(25);
        topBar.setStyle("-fx-background-color: rgb(216, 218, 219)");
        FlowPane rightIcons = new FlowPane();
        rightIcons.setPrefWidth(152);

        HBox h1 = createSquareIconBox("gear.png", 20, 9);

        HBox h2 = createSquareIconBox("minimize.png", 14, 12);

        HBox h3 = createSquareIconBox("Maximize.png", 14, 12);

        HBox h4 = createSquareIconBox("close.png", 14, 12);

        rightIcons.getChildren().addAll(h1,h2,h3,h4);
        topBar.getChildren().addAll(rightIcons);
        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);

    }

    private static HBox createSquareIconBox(String image, int size, int padding) {
        ImageView closeIcon = ImageUtils.getImageViewFromResources(image);
        ImageUtils.resize(closeIcon, size, size);
        HBox hbox = new HBox(closeIcon);
        hbox.setPadding(new Insets(padding));
        return hbox;
    }

    public void setAddressTab(AddressTab addressTab) {
        this.addressTab = addressTab;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AnchorPane getTopBar() {
        return topBar;
    }
}
