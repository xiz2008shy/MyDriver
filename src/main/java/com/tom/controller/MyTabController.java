package com.tom.controller;

import com.tom.utils.ImageUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class MyTabController {

    @FXML
    private ImageView iconImage;

    @FXML
    private Label title;

    @FXML
    private ImageView closeImage;

    @FXML
    private AnchorPane myAnchorPane;

    public void initialize() {
        // 绑定Image标签到控制器中的Image变量
        this.iconImage.setImage(ImageUtils.getImageFromResources("/img/fileDir32.png",32,32));
        this.closeImage.setImage(ImageUtils.getImageFromResources("/img/close.png", 16, 12));
        this.title.setText("title");
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
