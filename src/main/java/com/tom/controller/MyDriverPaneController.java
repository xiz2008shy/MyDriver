package com.tom.controller;

import com.tom.component.setting.MySetting;
import com.tom.handler.address.IconBakChangeHandler;
import com.tom.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

import java.io.File;

public class MyDriverPaneController {

    @FXML
    public Region backSvg;

    @FXML
    public HBox urlBox;


    public void initialize() {
        SVGPath svg = new SVGPath();
        svg.setContent("M512 0c281.6 0 512 230.4 512 512s-230.4 512-512 512-512-230.4-512-512 230.4-512 512-512z m0 960c249.6 0 448-198.4 448-448s-198.4-448-448-448-448 198.4-448 448 198.4 448 448 448z " +
                "M588.8 262.4c6.4-6.4 32-6.4 44.8 0 6.4 12.8 6.4 38.4 0 44.8L428.8 512l204.8 204.8c12.8 12.8 12.8 32 0 44.8-12.8 12.8-32 12.8-44.8 0L364.8 537.6c-12.8-12.8-12.8-32 0-44.8l224-230.4z");

        this.backSvg.setShape(svg);
        freshAddrTab(null);
        //HBox.setMargin(backSvg,new Insets(10,0,10,10));
        HBox.setMargin(urlBox,new Insets(0,0,0,15));
    }

    public void freshAddrTab(File curAddr) {
        urlBox.getChildren().clear();
        String basePath = MySetting.getConfig().getBasePath();
        if (curAddr == null){
            curAddr = new File(basePath);
        }
        addDirNodeExcludeBaseDir(urlBox, curAddr, basePath);
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
        Label dirLabel = new Label(file.getName());
        Font font = new Font(13);
        dirLabel.setFont(font);
        dirLabel.setPadding(new Insets(5));
        hBox.getChildren().add(dirLabel);
        HBox.setHgrow(dirLabel, Priority.ALWAYS);
        Tooltip tooltip = new Tooltip(file.getName());
        tooltip.getStyleClass().add("my-tooltip");
        dirLabel.setTooltip(tooltip);

        IconBakChangeHandler<Label> iconBakChangeHandler = new IconBakChangeHandler<>(file);
        dirLabel.addEventHandler(MouseEvent.MOUSE_ENTERED,iconBakChangeHandler);
        dirLabel.addEventHandler(MouseEvent.MOUSE_EXITED,iconBakChangeHandler);
        //dirLabel.addEventHandler(MouseEvent.MOUSE_CLICKED,new AddressJumpHandler(file,this));
        return true;
    }
}
