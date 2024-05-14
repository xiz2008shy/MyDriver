package com.tom.model;

import com.tom.controller.AddressPaneController;
import com.tom.controller.FileContentPaneController;
import com.tom.controller.MyDriverPaneController;
import com.tom.utils.FileNameUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
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

    /**
     * 搜索栏提示
     */
    private StringProperty tipsProperty = new SimpleStringProperty();

    /**
     * 当前选中的文件视图
     */
    private AnchorPane selectedFile;

    private File realSelectedFile;

    private Map<String,File> cacheMap = new HashMap<>();

    public ModelData(File file) {
        freshPage(file);
        tipsProperty.set(STR."在 \{FileNameUtil.getFileName(file)} 中搜索");
        curDirProperty.addListener((_,_,n) -> tipsProperty.set(STR."在 \{FileNameUtil.getFileName(n)} 中搜索"));
    }

    /**
     * 调用后会刷新地址栏和文件浏览页
     * @param curDir
     */
    public void freshPage(File curDir) {
        this.curDirProperty.set(curDir);
    }

    public File getCurDir() {
        return curDirProperty.get();
    }

    public ObjectProperty<File> curDirPropertyProperty() {
        return curDirProperty;
    }
}
