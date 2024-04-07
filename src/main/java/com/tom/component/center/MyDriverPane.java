package com.tom.component.center;

import com.tom.component.top.AddressTab;
import com.tom.pane.TabWatcher;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class MyDriverPane extends BorderPane implements TabWatcher<File> {

    private MainScrollPane mainScrollPane;

    private AddressTab addressTab;

    public MyDriverPane(AddressTab addressTab,MainScrollPane mainScrollPane) {
        this.mainScrollPane = mainScrollPane;
        this.addressTab = addressTab;
        this.setTop(addressTab.getAddressPane());
        this.setCenter(mainScrollPane);
        BorderPane.setMargin(mainScrollPane,new Insets(0,5,0,20));
    }

    @Override
    public Property<File> getWatcher() {
        return mainScrollPane.getWatcher();
    }

    @Override
    public String getInitTitle() {
        return mainScrollPane.getInitTitle();
    }

    @Override
    public String refreshTitle(File oldValue, File newValue) {
        return mainScrollPane.refreshTitle(oldValue,newValue);
    }
}
