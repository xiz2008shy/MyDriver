package com.tom.controller;

import cn.hutool.core.util.StrUtil;
import com.tom.config.MySetting;
import com.tom.handler.fxml.AddressJumpHandler;
import com.tom.model.ModelData;
import com.tom.utils.FileNameUtil;
import com.tom.utils.ImageUtils;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class AddressPaneController implements Initializable {

    @FXML
    private Region backSvg;

    @FXML
    private HBox urlBox;
    @FXML
    private TextField searchField;
    @FXML
    private ImageView searchIcon;

    @Getter
    @Setter
    private ModelData modelData;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("AddressPaneController initialize");
        SVGPath svg = new SVGPath();
        svg.setContent("M512 0c281.6 0 512 230.4 512 512s-230.4 512-512 512-512-230.4-512-512 230.4-512 512-512z m0 960c249.6 0 448-198.4 448-448s-198.4-448-448-448-448 198.4-448 448 198.4 448 448 448z " +
                "M588.8 262.4c6.4-6.4 32-6.4 44.8 0 6.4 12.8 6.4 38.4 0 44.8L428.8 512l204.8 204.8c12.8 12.8 12.8 32 0 44.8-12.8 12.8-32 12.8-44.8 0L364.8 537.6c-12.8-12.8-12.8-32 0-44.8l224-230.4z");

        this.backSvg.setShape(svg);
        this.searchIcon.setImage(ImageUtils.getImageFromResources("/img/searchIcon.png",32,32));

    }

    /**
     * 当modelData的curPath发生变化时，变更地址栏信息
     */
    public void doBind(){
        this.searchField.promptTextProperty().bind(modelData.getTipsProperty());
        freshAddrTab(modelData.getCurDirProperty().get());
        modelData.getCurDirProperty().addListener(this::freshListener);
    }


    private void freshListener(ObservableValue<?> observable, File oldValue, File newValue){
        freshAddrTab(newValue);
    }


    private void freshAddrTab(File file) {
        urlBox.getChildren().clear();
        String basePath = MySetting.getConfig().getBasePath();
        addDirNodeExcludeBaseDir(urlBox, file, basePath);
    }


    private boolean addDirNodeExcludeBaseDir(HBox hBox,File file,String basePath){
        String absolutePath = file.getAbsolutePath();
        if (absolutePath.contains(basePath)){
            if (!absolutePath.equals(basePath)){
                if(addDirNodeExcludeBaseDir(hBox,file.getParentFile(),basePath)) {
                    ImageView arrowView = ImageUtils.getImageViewFromResources("/img/right-arrow16.png",16,16);
                    ImageUtils.resize(arrowView,10,10);
                    hBox.getChildren().add(arrowView);
                    HBox.setMargin(arrowView, new Insets(0, 5, 0, 0));
                    return addAddrLabel(hBox, file);
                }
            }else {
                return addAddrLabel(hBox, file);
            }
        }
        return false;
    }

    private boolean addAddrLabel(HBox hBox, File file) {
        String labelName = FileNameUtil.getFileName(file);
        Label dirLabel = new Label(labelName);
        Font font = new Font(13);
        dirLabel.setFont(font);
        dirLabel.setPadding(new Insets(5));
        hBox.getChildren().add(dirLabel);
        HBox.setHgrow(dirLabel, Priority.ALWAYS);
        Tooltip tooltip = new Tooltip(file.getName());
        tooltip.getStyleClass().add("my-tooltip");
        dirLabel.setTooltip(tooltip);
        dirLabel.getStyleClass().add("my_addr_url");
        dirLabel.addEventHandler(MouseEvent.MOUSE_CLICKED,new AddressJumpHandler(file,modelData));
        return true;
    }


}
