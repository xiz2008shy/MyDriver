package com.tom.controller;

import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.config.vo.ConfigVo;
import com.tom.general.RecWindows;
import com.tom.handler.fxml.ConfigChangeCounter;
import com.tom.utils.ImageUtils;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MySettingController extends AnchorPane implements Initializable {


    @FXML
    private TextField basePath;
    @FXML
    private TextField remoteDBUrl;
    @FXML
    private TextField remoteDBUsername;
    @FXML
    private PasswordField remoteDBPwd;
    @FXML
    private HBox okBtn;
    @FXML
    private HBox applyBtn;
    @FXML
    private ImageView testResImg;
    @FXML
    private Label testConnection;

    @Getter @Setter
    private TextField focusedOn;

    @Getter
    private final IntegerProperty configChange = new SimpleIntegerProperty(0);

    /**
     * 当前窗口
     */
    @Setter @Getter
    private RecWindows windows;

    public MySettingController() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/MySetting.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshSettingPaneFromConfig();
        addFocusedCss(basePath);
        addFocusedCss(remoteDBUrl);
        addFocusedCss(remoteDBUsername);
        addFocusedCss(remoteDBPwd);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED,this::loseFocused);
        this.testConnection.addEventHandler(MouseEvent.MOUSE_RELEASED,MySetting.testConnection(this));
        this.okBtn.addEventHandler(MouseEvent.MOUSE_RELEASED,MySetting.okBtnClick(this));
        this.applyBtn.addEventHandler(MouseEvent.MOUSE_RELEASED,MySetting.applyBtnClick(this));

        this.configChange.addListener((_,_,n) ->{
            if ((int)n > 0){
                this.applyBtn.setDisable(false);
            }else {
                this.applyBtn.setDisable(true);
            }
        });
    }

    private int textFieldIndex = 0;
    private void addFocusedCss(TextField textField) {
        textField.focusedProperty().addListener((_, _, isNowFocused) -> {
            if (isNowFocused) {
                if (this.getFocusedOn() != null && !this.getFocusedOn().equals(textField)){
                    this.getFocusedOn().getParent().getStyleClass().remove("my_setting_tf_border_bottom");
                }
                textField.getParent().getStyleClass().add("my_setting_tf_border_bottom");
                this.setFocusedOn(textField);
            }
        });

        ConfigChangeCounter configChangeCounter = new ConfigChangeCounter(textField, textFieldIndex++, configChange);
        textField.textProperty().addListener(configChangeCounter);
    }


    public void loseFocused(MouseEvent event){
        if(this.getFocusedOn() != null){
            this.requestFocus();
            this.getFocusedOn().getParent().getStyleClass().remove("my_setting_tf_border_bottom");
        }
    }


    public void refreshConfig(){
        ConfigVo configVo = MySetting.getConfig();
        configVo.setBasePath(StrUtil.trim(basePath.getText()));
        configVo.setRemoteDBUrl(StrUtil.trim(remoteDBUrl.getText()));
        configVo.setRemoteDBUsername(StrUtil.trim(remoteDBUsername.getText()));
        configVo.setRemoteDBPwd(StrUtil.trim(remoteDBPwd.getText()));
    }


    public void refreshSettingPaneFromConfig(){
        ConfigVo configVo = MySetting.getConfig();
        basePath.setText(configVo.getBasePath());
        remoteDBUrl.setText(configVo.getRemoteDBUrl());
        remoteDBUsername.setText(configVo.getRemoteDBUsername());
        remoteDBPwd.setText(configVo.getRemoteDBPwd());
    }


    public boolean isConfigChange(){
        return this.configChange.get() > 0;
    }

    public void setTestImgRight(){
        this.testResImg.setImage(ImageUtils.getImageFromResources("/img/rt.png",32,32));
    }

    public void clearTestImg(){
        this.testResImg.setImage(null);
    }


    public void disableTestConnection(){
        this.testConnection.setDisable(true);
        this.testConnection.setCursor(Cursor.WAIT);
    }

    public void restoreTestConnection(){
        this.testConnection.setDisable(false);
        this.testConnection.setCursor(Cursor.HAND);
    }
}
