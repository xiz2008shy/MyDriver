package com.tom.general;

import com.tom.config.MySetting;
import com.tom.utils.ImageUtils;
import com.tom.utils.JDBCUtil;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class StatusBar extends HBox {

    private final StackPane stackPane = new StackPane();

    private ImageView statusImage;

    private final ImageView pointView = new ImageView();

    @Getter
    private final RecWindows windows;
    /**
     * 工作状态 1保持同步，0断开连接（不影响作业列表中的作业），2同步中
     */
    private AtomicInteger status = new AtomicInteger(0);

    private RotateTransition animation;

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
        try {
            if (!MySetting.isInitFactory()){
                JDBCUtil.createStableConnection();
                Platform.runLater(()-> {
                    switchStatus("/img/greenPoint.png",1);
                    switchToCloudIcon();
                });
            }else {
                Platform.runLater(()-> {
                    switchStatus("/img/greenPoint.png",1);
                    switchToCloudIcon();
                });
            }
        }catch (Exception e){
            log.error("StatusBar switchOnline occurred an error,cause: ",e);
            Platform.runLater(()-> {
                switchToCloudIcon();
                switchStatus("/img/redPoint.png",0);
                TipBlock.showDialog(e.getMessage() ,"Connection Failed!",windows.getStage());
            });
        }
    }

    private void switchToCloudIcon() {
        this.animation.pause();
        this.statusImage.setRotate(0);
        this.statusImage.setImage(ImageUtils.getImageFromResources("/img/syncSwitch.png",32,32));
        ImageUtils.resize(statusImage,24,24);
    }

    public void switchOffline (){
        animation.pause();
        statusImage.setRotate(0);
        statusImage.setImage(ImageUtils.getImageFromResources("/img/syncSwitch.png",32,32));
        ImageUtils.resize(statusImage,24,24);
        switchStatus("/img/redPoint.png",0);
    }

    public void switchSyncIcon(){
        switchStatus(null,status.get());
        statusImage.setImage(ImageUtils.getImageFromResources("/img/syncIcon32.png",32,32));
        ImageUtils.resize(statusImage,20,20);
        if (animation == null) {
            this.animation = new RotateTransition(Duration.millis(3000), statusImage);
            // 旋转度数
            animation.setByAngle(360f);
            animation.setCycleCount(Timeline.INDEFINITE);
        }
        animation.setAutoReverse(true);
        animation.play();
    }

    public void switchWaitingIcon(){
        switchStatus(null,status.get());
        statusImage.setImage(ImageUtils.getImageFromResources("/img/loading.png",32,32));
        ImageUtils.resize(statusImage,20,20);
        if (animation == null) {
            this.animation = new RotateTransition(Duration.millis(3000), statusImage);
            // 旋转度数
            animation.setByAngle(360);
            animation.setCycleCount(Timeline.INDEFINITE);
        }
        animation.setAutoReverse(false);
        animation.play();
    }

    public void switchLastStatus(){
        switchToCloudIcon();
        if (status.get() == 1){
            switchOnline();
        }else {
            switchOffline();
        }
        this.windows.freshPage();
    }

    private void switchStatus(String imgPath,int status) {
        if (imgPath == null) {
            this.pointView.setImage(null);
        }else {
            this.pointView.setImage(ImageUtils.getImageFromResources(imgPath,32,32));
        }
        this.status.set(status);
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
        return this.status.get() == 1;
    }
}
