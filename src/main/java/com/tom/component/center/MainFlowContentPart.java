package com.tom.component.center;

import cn.hutool.core.lang.UUID;
import com.tom.component.pub.DefaultAddressGetterImpl;
import com.tom.handler.icon.IconClickHandler;
import com.tom.model.AddressProperty;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainFlowContentPart extends DefaultAddressGetterImpl {

    private final FlowPane flowPane;

    private Map<String,File> pathIndex = new HashMap<>(128);

    private ObjectProperty<AnchorPane> os = new SimpleObjectProperty<>();

    public MainFlowContentPart(AddressProperty addressProperty) {
        super(addressProperty);
        this.flowPane = new FlowPane();
        flowPane.setStyle("-fx-background-color: white");
        refreshFileNode();
    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    public void refreshFileNode() {
        refreshFileNode(null);
    }

    public void refreshFileNode(Set<String> findFileSet) {
        ObservableList<Node> children = flowPane.getChildren();
        children.clear();
        pathIndex.clear();
        File baseDir = new File(getCurPath());
        File[] files = baseDir.listFiles();
        Set<AnchorPane> selectedSet = new HashSet<>();

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        assert files != null;
        for (File file : files) {
            AnchorPane anchorPane = genFileNode( fileSystemView,file);
            anchorPane.setPadding(new Insets(5,10,5,10));
            children.add(anchorPane);
            anchorPane.getStyleClass().add("my_icon");
            anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED,new IconClickHandler(os,selectedSet,file,this));
            if (findFileSet != null && findFileSet.contains(file.getName())){
                Event.fireEvent(anchorPane,new MouseEvent(MouseEvent.MOUSE_CLICKED,
                        1,1,1,1, MouseButton.PRIMARY, 1,
                        true, true, true, true,
                        true, true, true,
                        true, true, true, null));
            }
        }
    }


    private AnchorPane genFileNode( FileSystemView fileSystemView,File file) {
        String uuid = UUID.fastUUID().toString(true);
        pathIndex.put(uuid,file);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setId(uuid);
        FlowPane.setMargin(anchorPane,new Insets(10));
        anchorPane.setPrefWidth(90);
        anchorPane.setPrefHeight(90);
        //anchorPane.setStyle("-fx-background-color: #9b1dd3");
        VBox imageBox = new VBox();
        imageBox.setId(STR."\{uuid}_vbox");
        imageBox.setPrefHeight(60);
        imageBox.setAlignment(Pos.CENTER);
        //imageBox.setStyle("-fx-background-color: red");
        ImageView imageView;
        if (file.isDirectory()){
            imageView = ImageUtils.getImageViewFromResources("/img/fileDir32@2.png",60,60);
            imageBox.getChildren().add(imageView);
        }else {
            imageView = ImageUtils.getBigIcon(fileSystemView,file);
            imageBox.getChildren().add(imageView);
        }
        imageView.setId(STR."\{uuid}_vbox_img");
        Label label = new Label(file.getName());
        label.setMaxWidth(90);
        label.setStyle("-fx-text-overrun: ellipsis");
        Tooltip tooltip = new Tooltip(file.getName());
        tooltip.getStyleClass().add("my-tooltip");
        label.setTooltip(tooltip);
        HBox hBox = new HBox(label);
        hBox.setId(STR."\{uuid}_hBox");
        label.setId(STR."\{uuid}_hBox_label");
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(90);

        anchorPane.getChildren().addAll(imageBox,hBox);
        AnchorPaneUtil.setNode(imageBox,5.0,15.0,null,15.0);
        AnchorPaneUtil.setNode(hBox,65.0,0.0,null,0.0);

        return anchorPane;
    }

}
