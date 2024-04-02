package com.tom.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class AddressProperty {

    private StringProperty curPath;

    private ObjectProperty<File> file = new SimpleObjectProperty<>();

    public AddressProperty(String curPath) {
        this.curPath = new SimpleStringProperty(curPath);
        this.file.set(new File(curPath));
    }

    public String getCurPath() {
        return curPath.get();
    }

    public StringProperty curPathProperty() {
        return curPath;
    }

    public void setCurPath(String curPath) {
        this.curPath.set(curPath);
    }

    public File getFile() {
        return file.get();
    }

    public ObjectProperty<File> fileProperty() {
        return file;
    }
}
