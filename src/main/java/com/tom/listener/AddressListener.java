package com.tom.listener;

import com.tom.component.top.AddressTab;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;

public class AddressListener implements ChangeListener<File> {

    private AddressTab addressTab;

    public AddressListener(AddressTab mainAddressPart) {
        this.addressTab = mainAddressPart;
    }

    @Override
    public void changed(ObservableValue observable, File oldValue, File newValue) {
        addressTab.freshAddrTab(newValue);
    }
}
