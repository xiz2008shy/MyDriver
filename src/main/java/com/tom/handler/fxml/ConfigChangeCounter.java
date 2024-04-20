package com.tom.handler.fxml;

import com.tom.config.MySetting;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;


public class ConfigChangeCounter implements ChangeListener<String> {

    private int indexMask;
    private int indexOn;
    private TextField textField;
    private final IntegerProperty configChange;

    public ConfigChangeCounter(TextField textField,int textFieldIndex,IntegerProperty configChange) {
        this.textField = textField;
        this.configChange = configChange;
        this.indexOn = textFieldIndex > 0 ? 1 << (textFieldIndex - 1) : 1;
        this.indexMask = ~this.indexOn;
    }

    @Override
    public void changed(ObservableValue observable, String oldValue, String newValue) {
        String value = MySetting.getConfig().getValue(textField.getId());
        if (!value.equals(newValue)) {
            this.configChange.set(this.configChange.get() | indexOn);
        }else {
            this.configChange.set(this.configChange.get() & indexMask);
        }
    }
}
