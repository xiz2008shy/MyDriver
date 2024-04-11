package com.tom.general.menu;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.function.Predicate;


public class MyMenuContext extends HBox {

    private final BaseMenu baseMenu;

    private EventHandler<MouseEvent> mouseActiveHandler;
    private Predicate<MouseEvent> disabledPredicate;
    private Predicate<MouseEvent> visiblePredicate;

    public MyMenuContext(Node node,BaseMenu baseMenu) {
        super(node);
        this.baseMenu = baseMenu;
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(0,0,0, 20));
        baseMenu.addMenuContent(this);
        this.getStyleClass().add("my_menu_context");
        if (baseMenu.getMenuContent().getChildren().size() > 1){
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

    public Predicate<MouseEvent> getDisabledPredicate() {
        return disabledPredicate == null ? this::nonDisabled : disabledPredicate;
    }

    private boolean nonDisabled(MouseEvent e){
        return false;
    }

    private boolean visible(MouseEvent e){
        return true;
    }

    public void setDisabledPredicate(Predicate<MouseEvent> disabledPredicate) {
        this.disabledPredicate = disabledPredicate;
    }

    public Predicate<MouseEvent> getVisiblePredicate() {
        return visiblePredicate == null ? this::visible : visiblePredicate;
    }

    public void setVisiblePredicate(Predicate<MouseEvent> visiablePredicate) {
        this.visiblePredicate = visiablePredicate;
    }
}
