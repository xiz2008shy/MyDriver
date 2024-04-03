package com.tom.listener;

import com.tom.component.MainAddressPart;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;

public class AddressListener implements ChangeListener<File> {

    private MainAddressPart mainAddressPart;

    public AddressListener(MainAddressPart mainAddressPart) {
        this.mainAddressPart = mainAddressPart;
    }

    @Override
    public void changed(ObservableValue observable, File oldValue, File newValue) {
        mainAddressPart.freshAddrTab(newValue);
    }
}
