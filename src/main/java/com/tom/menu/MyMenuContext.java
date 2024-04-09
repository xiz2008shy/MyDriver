package com.tom.menu;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class MyMenuContext extends HBox {

    private final BaseMenu baseMenu;

    private EventHandler<MouseEvent> mouseActiveHandler;

    public MyMenuContext(Node node,BaseMenu baseMenu) {
        super(node);
        this.baseMenu = baseMenu;
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(0,0,0, 20));
        baseMenu.addMenuContent(this);
        this.getStyleClass().add("my_menu_context");
        if (baseMenu.getChildren().size() > 1){
            this.getStyleClass().add("top_border");
        }
    }

    private MyMenuContext(){
        this.baseMenu = null;
    }

    public void whenActiveByMouse(EventHandler<MouseEvent> handler){
        this.mouseActiveHandler = handler;
    }

    public EventHandler<MouseEvent> getMouseActiveHandler() {
        return mouseActiveHandler;
    }
}
