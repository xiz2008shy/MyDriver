package com.tom.controller;

import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.config.vo.ConfigVo;
import com.tom.general.RecWindows;
import com.tom.handler.fxml.ConfigChangeCounter;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

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

    @Getter @Setter
    private TextField focusedOn;

    @Setter @Getter
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
        this.okBtn.addEventHandler(MouseEvent.MOUSE_RELEASED,MySetting.saveConfig(this));

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
}
