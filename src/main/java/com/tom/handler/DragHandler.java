package com.tom.handler;

import com.tom.general.RecWindows;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * topBar的窗体拖拽监听器
 */
@Slf4j
public class DragHandler implements EventHandler<MouseEvent> {

    private double xOffset = 0;
    private double yOffset = 0;
    private final RecWindows windows;

    public DragHandler(RecWindows windows) {
        this.windows = windows;
    }

    @Override
    public void handle(MouseEvent event) {
        event.consume();
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && !windows.getStage().isMaximized()) {
            windows.getStage().setX(event.getScreenX() - xOffset);
            if(event.getScreenY() - yOffset < 0) {
                windows.getStage().setY(0);
            }else {
                windows.getStage().setY(event.getScreenY() - yOffset);
            }
        }
    }


    public void clickHandle(MouseEvent event) {
        event.consume();
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            RecWindows.maximizedOrRestore(windows);
        }
    }

}