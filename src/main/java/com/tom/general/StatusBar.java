package com.tom.general;

import com.tom.config.MySetting;
import com.tom.utils.ImageUtils;
import com.tom.utils.JDBCUtil;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
    private int status;

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
                int res = JDBCUtil.jdbcTest();
                if (res == 1){
                    JDBCUtil.createStableConnection();
                    switchStatus("/img/greenPoint.png",1);
                }else {
                    TipBlock.showDialog("连接失败，请稍后再试！","Connection Failed!",windows.getStage());
                }
            }else {
                switchStatus("/img/greenPoint.png",1);
            }
        }catch (Exception e){
            log.error("StatusBar switchOnline occurred an error,cause: ",e);
            TipBlock.showDialog(e.getMessage() ,"Connection Failed!",windows.getStage());
        }
    }

    public void switchOffline (){
        if (MySetting.isInitFactory()){

        }
        switchStatus("/img/redPoint.png",0);
    }

    public void switchSyncIcon(){
        switchStatus(null,status);
        statusImage.setImage(ImageUtils.getImageFromResources("/img/syncIcon32.png",32,32));
        ImageUtils.resize(statusImage,20,20);
        if (animation == null) {
            this.animation = new RotateTransition(Duration.millis(3000), statusImage);
            // 旋转度数
            animation.setByAngle(360f);
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.setAutoReverse(true);
        }

        animation.play();
    }

    public void switchLastStatus(){
        this.animation.pause();
        this.statusImage.setRotate(0);
        this.statusImage.setImage(ImageUtils.getImageFromResources("/img/syncSwitch.png",32,32));
        ImageUtils.resize(statusImage,24,24);
        if (status == 1){
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
