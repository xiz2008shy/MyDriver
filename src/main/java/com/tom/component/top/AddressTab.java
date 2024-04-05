package com.tom.component.top;

import com.tom.component.center.MainFlowContentPart;
import com.tom.component.pub.DefaultAddressGetterImpl;
import com.tom.handler.address.AddressJumpHandler;
import com.tom.handler.address.IconBakChangeHandler;
import com.tom.handler.address.IconColorChangeHandler;
import com.tom.listener.AddressListener;
import com.tom.model.AddressProperty;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.util.List;

public class AddressTab extends DefaultAddressGetterImpl {

    private final AnchorPane addressPane;

    private final MainFlowContentPart mainFlowContentPart;

    /**
     * url栏
     */
    private final HBox urlBox = new HBox();

    public AddressTab(AddressProperty addressProperty, MainFlowContentPart mainFlowContentPart) {
        super(addressProperty);
        this.addressPane = genAddressPane();
        this.mainFlowContentPart = mainFlowContentPart;
    }

    private AnchorPane genAddressPane(){
        AnchorPane addressPane = new AnchorPane();
        addressPane.setStyle("-fx-background-color: rgb(244, 244, 244)");

        // 返回按钮
        Region backSvg = ImageUtils.getBackSvg();
        assert backSvg != null;
        backSvg.setPrefSize(25,25);
        backSvg.setStyle("-fx-background-color: #777777;");

        IconColorChangeHandler<Region> handler = new IconColorChangeHandler<>(this);
        backSvg.addEventHandler(MouseEvent.MOUSE_ENTERED,handler);
        backSvg.addEventHandler(MouseEvent.MOUSE_EXITED,handler);
        backSvg.addEventHandler(MouseEvent.MOUSE_CLICKED, _ -> {
            AddressProperty addressProperty = getAddressProperty();
            File file = addressProperty.getFile();
            if (!file.getAbsolutePath().equals(addressProperty.getBasePath())) {
                getAddressProperty().setCurPath(file.getParent());
                mainFlowContentPart.refreshFileNode();
            }
        });
        HBox backBut = new HBox(backSvg);

        BorderStroke borderStroke = new BorderStroke(null, null, Color.rgb(191, 203, 217),
                null, null, null, BorderStrokeStyle.SOLID, null, null, BorderWidths.DEFAULT, null);
        addressPane.setBorder(new Border(borderStroke));

        // url栏
        File curAddr = this.getAddressProperty().getFile();
        this.getAddressProperty().fileProperty().addListener(new AddressListener(this));
        freshAddrTab(curAddr);

        HBox.setMargin(backSvg,new Insets(10,0,10,10));
        HBox.setMargin(urlBox,new Insets(0,0,0,15));
        urlBox.setPrefHeight(25);
        urlBox.setAlignment(Pos.CENTER_LEFT);

        // 搜索栏部分
        HBox searchTab = new HBox();
        searchTab.setAlignment(Pos.CENTER);
        TextField textField = new TextField();
        File file = new File(getCurPath());
        textField.setPromptText(STR."在 \{file.getName()} 中搜索");
        textField.setFocusTraversable(false);
        textField.setPrefWidth(200);
        ImageView searchIcon = ImageUtils.getImageViewFromResources("/img/searchIcon.png",32,32);
        ImageUtils.resize(searchIcon,20,20);
        searchTab.getChildren().addAll(textField,searchIcon);
        HBox.setMargin(textField,new Insets(10,0,10,15));
        HBox.setMargin(searchIcon,new Insets(10,15,10,10));

        addressPane.getChildren().addAll(backBut,urlBox,searchTab);
        AnchorPaneUtil.setNode(backBut,0.0,null,0.0,0.0);
        AnchorPaneUtil.setNode(urlBox,0.0,250.0,0.0,45.0);
        AnchorPaneUtil.setNode(searchTab,0.0,0.0,0.0,null);
        return addressPane;
    }

    /**
     * 刷新地址栏
     * @param curAddr 当前地址
     */
    public void freshAddrTab(File curAddr) {
        urlBox.getChildren().clear();
        addDirNodeExcludeBaseDir(urlBox, curAddr,getAddressProperty().getBasePath());
    }

    public AnchorPane getAddressPane() {
        return addressPane;
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
        HBox.setHgrow(dirLabel,Priority.ALWAYS);
        Tooltip tooltip = new Tooltip(file.getName());
        tooltip.getStyleClass().add("myToolTip");
        dirLabel.setTooltip(tooltip);

        IconBakChangeHandler<Label> iconBakChangeHandler = new IconBakChangeHandler<>(file);
        dirLabel.addEventHandler(MouseEvent.MOUSE_ENTERED,iconBakChangeHandler);
        dirLabel.addEventHandler(MouseEvent.MOUSE_EXITED,iconBakChangeHandler);
        dirLabel.addEventHandler(MouseEvent.MOUSE_CLICKED,new AddressJumpHandler(file,this));
        return true;
    }

    public void refreshFileNode(){
        this.mainFlowContentPart.refreshFileNode();
    }
}
