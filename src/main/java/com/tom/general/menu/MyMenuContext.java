package com.tom.general.menu;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;


public class MyMenuContext extends HBox {

    private final BaseMenu baseMenu;

    @Getter
    private EventHandler<MouseEvent> mouseActiveHandler;
    @Setter
    private Predicate<Event> disabledPredicate;
    @Setter
    private Predicate<Event> visiblePredicate;

    public MyMenuContext(BaseMenu baseMenu,Node... nodes) {
        super(nodes);
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

    public Predicate<Event> getDisabledPredicate() {
        return disabledPredicate == null ? this::nonDisabled : disabledPredicate;
    }

    private boolean nonDisabled(Event e){
        return false;
    }

    private boolean visible(Event e){
        return true;
    }

    public Predicate<Event> getVisiblePredicate() {
        return visiblePredicate == null ? this::visible : visiblePredicate;
    }

}
