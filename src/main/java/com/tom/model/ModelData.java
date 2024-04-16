package com.tom.model;

import com.tom.controller.AddressPaneController;
import com.tom.controller.FileContentPaneController;
import com.tom.controller.MyDriverPaneController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    private ObjectProperty<File> curDirProperty = new SimpleObjectProperty<>();
    private StringProperty curPathProperty = new SimpleStringProperty();
    private StringProperty tipsProperty = new SimpleStringProperty();

    /**
     * 当前选中的文件视图
     */
    private AnchorPane selectedFile;

    private File realSelectedFile;

    private Map<String,File> cacheMap = new HashMap<>();

    public ModelData(File file) {
        setFile(file);
        curPathProperty.bindBidirectional(curDirProperty, new StringConverter<>() {
            @Override
            public String toString(File file) {
                return file.getName();
            }

            @Override
            public File fromString(String path) {
                return new File(path);
            }
        });
        curDirProperty.addListener((_,_,n) -> tipsProperty.set(STR."在 \{n.getName()} 中搜索"));
    }

    public void setFile(File curDir) {
        this.curDirProperty.set(curDir);
    }

    public void setPath(String path) {
        this.curPathProperty.set(path);
    }

    public File getCurDir() {
        return curDirProperty.get();
    }

    public ObjectProperty<File> curDirPropertyProperty() {
        return curDirProperty;
    }
}
