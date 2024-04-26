package com.tom.general;

import com.tom.component.menu.StatusBarMenu;
import com.tom.utils.ImageUtils;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class StatusBar extends HBox {

    private final StackPane stackPane = new StackPane();

    private ImageView statusImage;

    private ImageView pointView;

    @Getter
    private final RecWindows windows;

    public StatusBar(RecWindows windows) {
        this.windows = windows;
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(60);
        this.setPrefHeight(10);
        this.setHeight(10);
        this.getChildren().add(stackPane);
        this.getStyleClass().add("my_status_bar");
        offlineStatus();
        addTrigger();
    }

    public void addTrigger() {
        StatusBarMenu.addMenuTrigger(this);
    }

    private void onlineStatus() {
        this.pointView = ImageUtils.getImageView("/img/greenPoint.png",32,10);
        this.pointView.setTranslateX(-13);
        this.pointView.setTranslateY(-7);

        this.statusImage = ImageUtils.getImageView("/img/syncSwitch.png",32,24);
        this.stackPane.getChildren().addAll(pointView,statusImage);
    }

    private void offlineStatus() {
        this.pointView = ImageUtils.getImageView("/img/redPoint.png",32,10);
        this.pointView.setTranslateX(-13);
        this.pointView.setTranslateY(-7);

        this.statusImage = ImageUtils.getImageView("/img/syncSwitch.png",32,24);
        this.stackPane.getChildren().addAll(pointView,statusImage);
    }
}
