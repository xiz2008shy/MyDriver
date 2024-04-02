package com.tom.component;

import com.tom.handler.icon.IconHandlerFactory;
import com.tom.handler.icon.IconHandlerFactoryBuilder;
import com.tom.model.AddressProperty;
import com.tom.utils.ImageUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class MainFlowContentPart {

    private FlowPane flowPane;

    private AddressProperty addressProperty;

    public MainFlowContentPart(AddressProperty addressProperty) {
        this.addressProperty = addressProperty;
        this.flowPane = new FlowPane();
        addFileNode(flowPane,addressProperty.getCurPath());
    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    private void addFileNode(FlowPane flowPane, String path) {
        ObservableList<Node> children = flowPane.getChildren();
        File baseDir = new File(path);
        File[] files = baseDir.listFiles();
        Set<AnchorPane> selectedSet = new HashSet<>();
        ObjectProperty<AnchorPane> os = new SimpleObjectProperty<>();
        IconHandlerFactoryBuilder<MouseEvent> instance = IconHandlerFactoryBuilder.getInstance(os, selectedSet);
        for (File file : files) {
            AnchorPane anchorPane = genFileNode( file);
            children.add(anchorPane);
            IconHandlerFactory<MouseEvent> factory = instance.createFactory(file);
            anchorPane.addEventFilter(MouseEvent.MOUSE_CLICKED, factory.getIconClickHandler());
            anchorPane.addEventFilter(MouseEvent.MOUSE_ENTERED, factory.getIconMouseInHandler());
            anchorPane.addEventFilter(MouseEvent.MOUSE_EXITED, factory.getIconMouseOutHandler());
        }
    }


    private AnchorPane genFileNode( File file) {
        AnchorPane anchorPane = new AnchorPane();
        FlowPane.setMargin(anchorPane,new Insets(10));
        anchorPane.setPrefWidth(90);
        anchorPane.setPrefHeight(90);
        //anchorPane.setStyle("-fx-background-color: #9b1dd3");
        VBox imageBox = new VBox();
        imageBox.setPrefHeight(60);
        imageBox.setAlignment(Pos.CENTER);
        //imageBox.setStyle("-fx-background-color: red");
        ImageView imageView;
        if (file.isDirectory()){
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("fileDir.png");
            Image image = new Image(inputStream);
            imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageBox.getChildren().add(imageView);
        }else {
            imageView = ImageUtils.getBigIcon( file);
            imageBox.getChildren().add(imageView);
        }

        Label label = new Label(file.getName());
        label.setMaxWidth(90);
        label.setStyle("-fx-text-overrun: ellipsis");
        HBox hBox = new HBox(label);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(90);

        anchorPane.getChildren().addAll(imageBox,hBox);
        AnchorPane.setTopAnchor(imageBox,5.0);
        AnchorPane.setLeftAnchor(imageBox,15.0);
        AnchorPane.setRightAnchor(imageBox,15.0);
        AnchorPane.setTopAnchor(hBox,65.0);
        AnchorPane.setLeftAnchor(hBox,0.0);
        AnchorPane.setRightAnchor(hBox,0.0);
        return anchorPane;
    }



}
