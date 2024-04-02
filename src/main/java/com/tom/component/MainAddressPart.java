package com.tom.component;

import com.tom.model.AddressProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.InputStream;

public class MainAddressPart {

    private AnchorPane addressPane;

    private AddressProperty addressProperty;

    public MainAddressPart(AddressProperty addressProperty) {
        this.addressProperty = addressProperty;
        this.addressPane = genAddressPane();
    }

    private AnchorPane genAddressPane(){
        AnchorPane anchorPane = new AnchorPane();
        HBox hBox = new HBox();
        Text text = new Text(addressProperty.getCurPath());
        Font font = new Font(13);
        text.setFont(font);
        hBox.getChildren().add(text);
        HBox.setMargin(text,new Insets(10,0,10,15));
        hBox.setPrefHeight(25);
        hBox.setAlignment(Pos.CENTER_LEFT);

        HBox searchTab = new HBox();
        searchTab.setAlignment(Pos.CENTER);
        TextField textField = new TextField();
        File file = new File(addressProperty.getCurPath());
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

    public AnchorPane getAddressPane() {
        return addressPane;
    }
}
