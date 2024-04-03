package com.tom.handler.address;

import com.tom.component.MainAddressPart;
import com.tom.model.AddressProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class IconColorChangeHandler<T extends Node> implements EventHandler<MouseEvent> {

    private boolean bp = true;

    private MainAddressPart mainAddressPart;

    public IconColorChangeHandler(MainAddressPart mainAddressPart) {
        this.mainAddressPart = mainAddressPart;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        Node source = (T)mouseEvent.getSource();
        AddressProperty addressProperty = mainAddressPart.getAddressProperty();
        if (!addressProperty.getCurPath().equals(addressProperty.getBasePath())) {
            if (bp) {
                source.setStyle("-fx-background-color: #1c94cf;");
            }else {
                source.setStyle("-fx-background-color: #777777;");
            }
            this.bp = !bp;
        }else {
            this.bp = true;
            source.setStyle("-fx-background-color: #777777;");
        }
    }

}
