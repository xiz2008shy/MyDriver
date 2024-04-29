package com.tom.handler.fxml;

import com.tom.config.MySetting;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import java.util.Map;


public class ConfigChangeCounter implements ChangeListener<Object> {

    private int indexMask;
    private int indexOn;
    private Node node;
    private final IntegerProperty configChange;

    public ConfigChangeCounter(Node node,int textFieldIndex,IntegerProperty configChange) {
        this.node = node;
        this.configChange = configChange;
        this.indexOn = textFieldIndex > 0 ? 1 << (textFieldIndex - 1) : 1;
        this.indexMask = ~this.indexOn;
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        String value = MySetting.getConfig().getValue(node.getId());
        if (newValue instanceof Map.Entry entry) {
            newValue = entry.getKey();
        }

        if (!newValue.equals(value)) {
            this.configChange.set(this.configChange.get() | indexOn);
        }else {
            this.configChange.set(this.configChange.get() & indexMask);
        }
    }
}
