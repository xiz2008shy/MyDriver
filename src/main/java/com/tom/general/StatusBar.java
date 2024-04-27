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

    private final ImageView pointView = new ImageView();

    @Getter
    private final RecWindows windows;
    /**
     * 工作状态 1保持同步，0断开连接（不影响作业列表中的作业），2立即同步
     */
    private int status;

    public StatusBar(RecWindows windows) {
        this.windows = windows;
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(60);
        this.setPrefHeight(10);
        this.setHeight(10);
        this.getChildren().add(stackPane);
        this.getStyleClass().add("my_status_bar");
        initStatus();
    }


    public void switchOnline (){
        switchStatus("/img/greenPoint.png",1);
    }

    public void switchOffline (){
        switchStatus("/img/redPoint.png",0);
    }

    private void switchStatus(String imgPath,int status) {
        this.pointView.setImage(ImageUtils.getImageFromResources(imgPath,32,32));
        this.status = status;
    }

    private void initStatus() {
        this.pointView.setImage(ImageUtils.getImageFromResources("/img/redPoint.png",32,32));
        ImageUtils.resize(pointView,10,10);
        this.pointView.setTranslateX(-13);
        this.pointView.setTranslateY(-7);

        this.statusImage = ImageUtils.getImageView("/img/syncSwitch.png",32,24);
        this.stackPane.getChildren().addAll(pointView,statusImage);
    }

    public boolean isOnline(){
        return this.status == 1;
    }
}
