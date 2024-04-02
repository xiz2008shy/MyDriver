package com.tom.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AddressProperty {

    private StringProperty curPath;

    public AddressProperty(String curPath) {
        this.curPath = new SimpleStringProperty(curPath);
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
}
