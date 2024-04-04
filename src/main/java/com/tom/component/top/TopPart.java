package com.tom.component.top;

import com.tom.component.center.MainFlowContentPart;
import com.tom.listener.DragListener;
import com.tom.model.AddressProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopPart {

    private final TopBar topBar;

    private final AddressTab addressTab;

    private final VBox topPart;

    public TopPart(AddressProperty addressProperty, MainFlowContentPart mainFlowContentPart, Stage stage) {
        this.topBar = new TopBar();
        this.addressTab = new AddressTab(addressProperty,mainFlowContentPart);
        topBar.setAddressTab(addressTab);
        topPart = new VBox();
        topPart.getChildren().addAll(topBar.getTopBar(),addressTab.getAddressPane());
        DragListener dragListener = new DragListener(stage);
        topBar.getTopBar().addEventHandler(MouseEvent.MOUSE_PRESSED,dragListener);
        topBar.getTopBar().addEventHandler(MouseEvent.MOUSE_DRAGGED,dragListener);
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
