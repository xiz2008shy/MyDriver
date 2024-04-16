package com.tom.handler.fxml;

import com.tom.model.ModelData;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class AddressJumpHandler implements EventHandler<MouseEvent> {

    private final File file;

    private final ModelData modelData;

    public AddressJumpHandler(File file, ModelData modelData) {
        this.file = file;
        this.modelData = modelData;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (!modelData.getCurDirProperty().get().equals(file.getAbsolutePath())) {
                this.modelData.setFile(file);
            }
        }
    }
}
