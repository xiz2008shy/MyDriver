package com.tom.component;

import com.tom.handler.address.AddressJumpHandler;
import com.tom.handler.address.IconBakChangeHandler;
import com.tom.handler.address.IconColorChangeHandler;
import com.tom.listener.AddressListener;
import com.tom.model.AddressProperty;
import com.tom.utils.ImageUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.File;
import java.io.InputStream;

public class MainAddressPart extends DefaultAddressGetterImpl{

    private AnchorPane addressPane;

    private MainFlowContentPart mainFlowContentPart;

    /**
     * url栏
     */
    private HBox urlBox = new HBox();

    public MainAddressPart(AddressProperty addressProperty,MainFlowContentPart mainFlowContentPart) {
        super(addressProperty);
        this.addressPane = genAddressPane();
        this.mainFlowContentPart = mainFlowContentPart;
    }

    private AnchorPane genAddressPane(){
        AnchorPane anchorPane = new AnchorPane();
        HBox hBox = new HBox();

        // 返回按钮
        Region backSvg = ImageUtils.getSvgFromResources(this.getClass().getClassLoader(),"back.svg");
        backSvg.setPrefSize(25,25);
        backSvg.setStyle("-fx-background-color: #777777;");
        IconColorChangeHandler<Region> handler = new IconColorChangeHandler<>(this);
        backSvg.addEventHandler(MouseEvent.MOUSE_ENTERED,handler);
        backSvg.addEventHandler(MouseEvent.MOUSE_EXITED,handler);
        backSvg.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            AddressProperty addressProperty = getAddressProperty();
            File file = addressProperty.getFile();
            if (!file.getAbsolutePath().equals(addressProperty.getBasePath())) {
                getAddressProperty().setCurPath(file.getParent());
                mainFlowContentPart.refreshFileNode();
            }
        });

        // url栏
        urlBox.setAlignment(Pos.CENTER_LEFT);
        File curAddr = this.getAddressProperty().getFile();
        this.getAddressProperty().fileProperty().addListener(new AddressListener(this));

        freshAddrTab(curAddr);

        hBox.getChildren().addAll(backSvg,urlBox);

        HBox.setMargin(backSvg,new Insets(10,0,10,15));
        HBox.setMargin(urlBox,new Insets(0,0,0,15));
        hBox.setPrefHeight(25);
        hBox.setAlignment(Pos.CENTER_LEFT);

        // 搜索栏部分
        HBox searchTab = new HBox();
        searchTab.setAlignment(Pos.CENTER);
        TextField textField = new TextField();
        File file = new File(getCurPath());
        textField.setPromptText("在 " +file.getName()+" 中搜索");
        textField.setFocusTraversable(false);
        textField.setPrefWidth(200);
        InputStream iconInputStream = this.getClass().getClassLoader().getResourceAsStream("searchIcon.png");
        ImageView searchIcon = new ImageView(new Image(iconInputStream));
        searchIcon.setFitWidth(20);
        searchIcon.setFitHeight(20);
        searchTab.getChildren().addAll(textField,searchIcon);
        HBox.setMargin(textField,new Insets(10,0,10,15));
        HBox.setMargin(searchIcon,new Insets(10,15,10,10));

        anchorPane.getChildren().addAll(hBox,searchTab);
        AnchorPane.setLeftAnchor(hBox,0.0);
        AnchorPane.setTopAnchor(hBox,0.0);
        AnchorPane.setBottomAnchor(hBox,0.0);

        AnchorPane.setRightAnchor(searchTab,0.0);
        AnchorPane.setTopAnchor(searchTab,0.0);
        AnchorPane.setBottomAnchor(searchTab,0.0);
        return anchorPane;
    }

    /**
     * 刷新地址栏
     * @param curAddr
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
                    ImageView arrowView = ImageUtils.getImageViewFromResources("right-arrow16.png");
                    arrowView.setFitHeight(10);
                    arrowView.setFitWidth(10);
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

        IconBakChangeHandler iconBakChangeHandler = new IconBakChangeHandler(file);
        dirLabel.addEventHandler(MouseEvent.MOUSE_ENTERED,iconBakChangeHandler);
        dirLabel.addEventHandler(MouseEvent.MOUSE_EXITED,iconBakChangeHandler);
        dirLabel.addEventHandler(MouseEvent.MOUSE_CLICKED,new AddressJumpHandler(file,this));
        return true;
    }

    public void refreshFileNode(){
        this.mainFlowContentPart.refreshFileNode();
    }
}
