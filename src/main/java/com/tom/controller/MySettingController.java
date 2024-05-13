package com.tom.controller;

import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.config.vo.ConfigVo;
import com.tom.general.RecWindows;
import com.tom.handler.fxml.ConfigChangeCounter;
import com.tom.model.PropData;
import com.tom.utils.FileNameUtil;
import com.tom.utils.ImageUtils;
import com.tom.utils.PropUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
    private ComboBox<Map.Entry<Object, String>> ossProvider;
    @FXML
    private ComboBox<Map.Entry<Object, String>> ossEndpoint;
    @FXML
    private TextField bucketName;
    @FXML
    private TextField accessKeyId;
    @FXML
    private TextField accessKeySecret;

    @FXML
    private HBox okBtn;
    @FXML
    private HBox applyBtn;
    @FXML
    private ImageView testResImg;
    @FXML
    private Label testConnection;

    @Getter @Setter
    private Node focusedOn;

    @Getter
    private final IntegerProperty configChange = new SimpleIntegerProperty(0);
    @Getter
    private final RecWindows fromWindow;

    /**
     * 当前窗口
     */
    @Setter @Getter
    private RecWindows windows;

    private PropData ossProp;

    private PropData aliyunOssRegion;

    public MySettingController(RecWindows fromWindows) {
        this.fromWindow = fromWindows;
        this.ossProp = PropUtils.ossProvider();
        this.aliyunOssRegion = PropUtils.aliyunOssRegion();
        Stage utility = new Stage();
        utility.initStyle(StageStyle.UTILITY);
        utility.setOpacity(0);
        Stage settingStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/MySetting.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RecWindows settingWindows = new RecWindows(this, 600.0,
                600.0, 12.0, settingStage,"setting",1);
        settingWindows.setFromWindows(fromWindows);
        settingWindows.initStage();
        this.setWindows(settingWindows);
        settingStage.initModality(Modality.APPLICATION_MODAL);
        settingStage.initOwner(utility);
        utility.show();
        settingStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshSettingPaneFromConfig();
        addFocusedCss(basePath);
        addFocusedCss(remoteDBUrl);
        addFocusedCss(remoteDBUsername);
        addFocusedCss(remoteDBPwd);
        addFocusedCss(bucketName);
        addFocusedCss(accessKeyId);
        addFocusedCss(accessKeySecret);
        addFocusedCss(ossProvider);
        addFocusedCss(ossEndpoint);

        this.addEventHandler(MouseEvent.MOUSE_PRESSED,this::loseFocused);
        this.testConnection.addEventHandler(MouseEvent.MOUSE_RELEASED,MySetting.testConnection(this));
        this.okBtn.addEventHandler(MouseEvent.MOUSE_RELEASED,MySetting.okBtnClick(this));
        this.applyBtn.addEventHandler(MouseEvent.MOUSE_RELEASED,MySetting.applyBtnClick(this));

        this.ossProvider.setItems(ossProp.list());
        this.ossProvider.setConverter(PropUtils.getPropConverter());

        this.ossEndpoint.setItems(aliyunOssRegion.list());
        this.ossEndpoint.setConverter(PropUtils.getPropConverter());

        this.configChange.addListener((_,_,n) ->{
            if ((int)n > 0){
                this.applyBtn.setDisable(false);
            }else {
                this.applyBtn.setDisable(true);
            }
        });
    }

    private int textFieldIndex = 0;
    private void addFocusedCss(Node node) {
        node.focusedProperty().addListener((_, _, isNowFocused) -> {
            if (isNowFocused) {
                if (this.getFocusedOn() != null && !this.getFocusedOn().equals(node)){
                    this.getFocusedOn().getParent().getStyleClass().remove("my_setting_tf_border_bottom");
                }
                node.getParent().getStyleClass().add("my_setting_tf_border_bottom");
                this.setFocusedOn(node);
            }
        });

        ConfigChangeCounter configChangeCounter = new ConfigChangeCounter(node, textFieldIndex++, configChange);
        if (node instanceof TextField textField) {
            textField.textProperty().addListener(configChangeCounter);
        }else if (node instanceof ComboBox comboBox) {
            comboBox.getSelectionModel().selectedItemProperty().addListener(configChangeCounter);
        }

    }


    public void loseFocused(MouseEvent event){
        if(this.getFocusedOn() != null){
            this.requestFocus();
            this.getFocusedOn().getParent().getStyleClass().remove("my_setting_tf_border_bottom");
        }
    }


    /**
     * 更新setting的实体对象
     */
    public void refreshConfig(){
        ConfigVo configVo = MySetting.getConfig();
        if (!StrUtil.equals(configVo.getBasePath(),basePath.getText())){
            configVo.setBasePath(StrUtil.trim(basePath.getText()));
            this.getFromWindow().closeInActiveTab();
            this.getFromWindow().getActiveModelData().getCurDirProperty().set(new File(basePath.getText()));
        }
        configVo.setRemoteDBUrl(StrUtil.trim(remoteDBUrl.getText()));
        configVo.setRemoteDBUsername(StrUtil.trim(remoteDBUsername.getText()));
        configVo.setRemoteDBPwd(StrUtil.trim(remoteDBPwd.getText()));
        configVo.setAccessKeyId(StrUtil.trim(accessKeyId.getText()));
        configVo.setAccessKeySecret(StrUtil.trim(accessKeySecret.getText()));
        configVo.setBucketName(StrUtil.trim(bucketName.getText()));
        configVo.setOssProvider((String) ossProvider.getValue().getKey());
        configVo.setOssEndpoint((String) ossEndpoint.getValue().getKey());
    }


    public boolean validBasePath(){
        String path = basePath.getText();
        if (StrUtil.isNotBlank(path)){
            if (!FileNameUtil.containChinese(path)){
                return new File(path).exists();
            }else {
                path = path.replaceAll("\\\\","\\\\");
                File originFile = new File(path);
                return originFile.exists();
            }
        }
        return false;
    }





    /**
     * 更新设置面板的属性
     */
    public void refreshSettingPaneFromConfig(){
        ConfigVo configVo = MySetting.getConfig();
        basePath.setText(configVo.getBasePath());
        remoteDBUrl.setText(configVo.getRemoteDBUrl());
        remoteDBUsername.setText(configVo.getRemoteDBUsername());
        remoteDBPwd.setText(configVo.getRemoteDBPwd());
        bucketName.setText(configVo.getBucketName());
        accessKeyId.setText(configVo.getAccessKeyId());
        accessKeySecret.setText(configVo.getAccessKeySecret());
        ossProvider.getSelectionModel().select(ossProp.getProperty(configVo.getOssProvider()));
        ossEndpoint.getSelectionModel().select(aliyunOssRegion.getProperty(configVo.getOssEndpoint()));
    }


    public boolean isConfigChange(){
        return this.configChange.get() > 0;
    }

    public void setTestImgRight(){
        this.testResImg.setImage(ImageUtils.getImageFromResources("/img/rt.png",32,32));
    }

    public void setTestImgLoading(){
        this.testResImg.setImage(ImageUtils.getImageFromResources("/img/loading.png",32,32));
    }

    public void setTestImgError(){
        this.testResImg.setImage(ImageUtils.getImageFromResources("/img/error.png",32,32));
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
