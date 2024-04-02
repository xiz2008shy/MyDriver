package com.tom.component;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;



public class MainScrollPart {

    private ScrollPane scrollPane;

    public MainScrollPart(FlowPane flowPane) {
        this.scrollPane = genScrollPane(flowPane);
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
