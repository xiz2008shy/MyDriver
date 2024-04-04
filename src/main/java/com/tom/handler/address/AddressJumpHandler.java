package com.tom.handler.address;

import com.tom.component.top.AddressTab;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class AddressJumpHandler implements EventHandler<MouseEvent> {

    private File file;

    private AddressTab addressTab;

    public AddressJumpHandler(File file, AddressTab mainAddressPart) {
        this.file = file;
        this.addressTab = mainAddressPart;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (!addressTab.getAddressProperty().getCurPath().equals(file.getAbsolutePath())) {
                this.addressTab.getAddressProperty().setCurPath(file);
                this.addressTab.refreshFileNode();
            }
        }
    }
}
