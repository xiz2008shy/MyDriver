package com.tom.component.center;

import com.tom.handler.key.CopyHandler;
import com.tom.general.TabWatcher;
import javafx.beans.property.Property;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;

import java.io.File;


public class MainScrollPane extends ScrollPane implements TabWatcher<File> {

    private final MainFlowContentPart mainFlowContentPart;

    public MainScrollPane(MainFlowContentPart mainFlowContentPart) {
        this.mainFlowContentPart = mainFlowContentPart;
        this.setContent(mainFlowContentPart.getFlowPane());
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.setStyle("-fx-padding: 0;-fx-background-insets: 0;-fx-background-color: white;");
        this.addEventHandler(KeyEvent.KEY_PRESSED, new CopyHandler(mainFlowContentPart));
    }


    @Override
    public Property<File> getWatcher() {
        return mainFlowContentPart.getAddressProperty().fileProperty();
    }

    @Override
    public String getInitTitle() {
        return mainFlowContentPart.getAddressProperty().getFile().getName();
    }

    @Override
    public String refreshTitle(File oldValue, File newValue) {
        return newValue.getName();
    }

    public FlowPane getMainFlowPane() {
        return mainFlowContentPart.getFlowPane();
    }

    public MainFlowContentPart getMainFlowContentPart() {
        return mainFlowContentPart;
    }
}
