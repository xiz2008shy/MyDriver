package com.tom.component.top;

import com.tom.component.center.MainFlowContentPart;
import com.tom.model.AddressProperty;
import javafx.scene.layout.VBox;

public class TopPart {

    private final TopBar topBar;

    private final AddressTab addressTab;

    private final VBox topPart;

    public TopPart(AddressProperty addressProperty, MainFlowContentPart mainFlowContentPart) {
        this.topBar = new TopBar();
        this.addressTab = new AddressTab(addressProperty,mainFlowContentPart);
        topBar.setAddressTab(addressTab);
        topPart = new VBox();
        topPart.getChildren().addAll(topBar.getTopBar(),addressTab.getAddressPane());
    }


    public TopBar getTopBar() {
        return topBar;
    }

    public AddressTab getAddressTab() {
        return addressTab;
    }

    public VBox getTopPart() {
        return topPart;
    }

    public void setTitle(String title){
        topBar.setTitle(title);
    }
}
