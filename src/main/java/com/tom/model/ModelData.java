package com.tom.model;

import com.tom.component.setting.MySetting;
import com.tom.controller.AddressPaneController;
import com.tom.controller.FileContentPaneController;
import com.tom.controller.MyDriverPaneController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;

@Accessors(chain = true)
@Getter
@Setter
public class ModelData {

    /**
     * 整体控制器
     */
    private MyDriverPaneController myDriverPaneController;

    /**
     * 地址栏控制器
     */
    private AddressPaneController addressPaneController;

    /**
     * 文件浏览内容控制器
     */
    private FileContentPaneController fileContentPaneController;

    /**
     * 当前文件夹
     */
    private ObjectProperty<File> curDir = new SimpleObjectProperty<>();
    private StringProperty curPath = new SimpleStringProperty();

    /**
     * 当前选中的文件视图
     */
    private AnchorPane selectedFile;

    public ModelData(File curDir) {
        setFile(curDir);
    }

    public void setFile(File curDir) {
        this.curDir.set(curDir);
        this.curPath.set(curDir.getAbsolutePath());
    }

    public void setPath(String path) {
        this.curDir.set(new File(path));
        this.curPath.set(path);
    }
}
