package com.tom.pane;

import com.tom.listener.DragListener;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.DrawUtil;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RecWindows extends AnchorPane {

    private final Rectangle rectangle;
    private final TopBar topBar;
    private String title;

    public RecWindows( Node node, double prefWidth, double prefHeight, double radius) {
        super();
        this.rectangle = new Rectangle(prefWidth,prefHeight);
        rectangle.setArcWidth(radius);
        rectangle.setArcHeight(radius);
        this.setShape(rectangle);
        this.setPrefSize(prefWidth,prefHeight);
        this.setClip(rectangle);
        this.topBar = new TopBar();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(topBar.getTopBar(),node);

        this.getChildren().add(vBox);

        AnchorPaneUtil.setNode(vBox,0.0,0.0,5.0,0.0);
    }

    public void initStage(Stage stage){
        Scene scene = new Scene(this);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setWidth(rectangle.getWidth());
        stage.setHeight(rectangle.getHeight());
        // 添加窗体拉伸效果
        DrawUtil.addDrawFunc(stage, this);
        DragListener dragListener = new DragListener(stage);
        topBar.getTopBar().addEventHandler(MouseEvent.MOUSE_PRESSED,dragListener);
        topBar.getTopBar().addEventHandler(MouseEvent.MOUSE_DRAGGED,dragListener);
    }


    public void myResize(double prefWidth, double prefHeight){
        rectangle.setWidth(prefWidth);
        rectangle.setHeight(prefHeight);
        this.setPrefSize(prefWidth,prefHeight);
    }

    public void setTitle(String title){
        topBar.setTitle(title);
    }
}
