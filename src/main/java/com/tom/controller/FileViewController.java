package com.tom.controller;

import cn.hutool.core.lang.UUID;
import com.tom.handler.fxml.DesktopIconClickHandler;
import com.tom.model.ModelData;
import com.tom.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FileViewController extends AnchorPane implements Initializable {

    @FXML
    private VBox imageBox;

    @FXML
    private ImageView imageView;

    @FXML
    private Label fileNameLabel;

    @FXML
    private HBox fileNameBox;

    private final File file;

    private ModelData modelData;

    public FileViewController(File file, ModelData modelData) {
        this.file = file;
        this.modelData = modelData;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED,new DesktopIconClickHandler(file,modelData));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/OriginFileView.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (file.isDirectory()){
            imageView.setImage(ImageUtils.getImageFromResources("/img/fileDir32@2.png",60,60));
        }else {
            FileSystemView fileSystemView = FileSystemView.getFileSystemView();
            imageView.setImage(ImageUtils.getBigIconImage(fileSystemView,file));
        }
        fileNameLabel.setText(file.getName());

        String uuid = UUID.fastUUID().toString(true);
        imageBox.setId(STR."\{uuid}_imgBox");
        imageView.setId(STR."\{uuid}_imageView");
        fileNameLabel.setId(STR."\{uuid}_fileNameLabel");
        fileNameBox.setId(STR."\{uuid}_fileNameBox");
        this.setId(uuid);
        modelData.getCacheMap().put(uuid,file);
    }
}
