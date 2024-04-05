package com.tom.component.center;

import com.tom.handler.key.CopyHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;



public class MainScrollPart {

    private MainFlowContentPart mainFlowContentPart;
    private ScrollPane scrollPane;

    public MainScrollPart(MainFlowContentPart mainFlowContentPart) {
        this.scrollPane = genScrollPane(mainFlowContentPart.getFlowPane());
        scrollPane.setStyle("-fx-padding: 0;-fx-background-insets: 0;-fx-background-color: white;");
        scrollPane.addEventHandler(KeyEvent.KEY_PRESSED, new CopyHandler(mainFlowContentPart));
    }

    private ScrollPane genScrollPane(FlowPane flowPane) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
