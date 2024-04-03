package com.tom.handler.address;

import com.tom.component.MainAddressPart;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class AddressJumpHandler implements EventHandler<MouseEvent> {

    private File file;

    private MainAddressPart mainAddressPart;

    public AddressJumpHandler(File file, MainAddressPart mainAddressPart) {
        this.file = file;
        this.mainAddressPart = mainAddressPart;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (!mainAddressPart.getAddressProperty().getFile().equals(file)) {
                this.mainAddressPart.getAddressProperty().setCurPath(file);
                this.mainAddressPart.refreshFileNode();
            }
        }
    }
}
