package com.tom.controller;

import com.tom.component.setting.MySetting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MySettingController extends AnchorPane implements Initializable {


    @FXML
    private TextField basePath;
    @FXML
    private TextField mysqlUrl;
    @FXML
    private TextField username;
    @FXML
    private PasswordField pwd;

    @Getter @Setter
    private TextField focusedOn;

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
        basePath.setText(MySetting.getConfig().getBasePath());
        addFocusedCss(basePath);
        addFocusedCss(mysqlUrl);
        addFocusedCss(username);
        addFocusedCss(pwd);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED,this::loseFocused);
    }

    private void addFocusedCss(TextField textField) {
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                if (this.getFocusedOn() != null && !this.getFocusedOn().equals(textField)){
                    this.getFocusedOn().getParent().getStyleClass().remove("my_setting_tf_border_bottom");
                }
                textField.getParent().getStyleClass().add("my_setting_tf_border_bottom");
                this.setFocusedOn(textField);
            }
        });
    }


    public void loseFocused(MouseEvent event){
        if(this.getFocusedOn() != null){
            this.requestFocus();
            this.getFocusedOn().getParent().getStyleClass().remove("my_setting_tf_border_bottom");
        }
    }
}
