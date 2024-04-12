package com.tom.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class AddressProperty {

    private final String basePath;

    private StringProperty curPath;

    private ObjectProperty<File> file = new SimpleObjectProperty<>();

    public AddressProperty(String curPath,String basePath) {
        this.basePath = basePath;
        this.curPath = new SimpleStringProperty(curPath);
        this.file.set(new File(curPath));
    }

    public String getBasePath() {
        return basePath;
    }

    public String getCurPath() {
        return curPath.get();
    }

    public StringProperty getCurPathProperty() {
        return curPath;
    }

    /**
     * 必须通过setCurPath方法去修改当前地址，才能在地址栏和文件界面同时刷新
     * @param curPath
     */
    public void setCurPath(String curPath) {
        this.curPath.set(curPath);
        this.file.set(new File(curPath));
    }

    public void setCurPath(File file) {
        this.curPath.set(file.getAbsolutePath());
        this.file.set(file);
    }

    public File getFile() {
        return file.get();
    }



    public ObjectProperty<File> fileProperty() {
        return file;
    }
}
