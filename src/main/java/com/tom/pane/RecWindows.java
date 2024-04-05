package com.tom.pane;

import com.tom.listener.DragListener;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.DrawUtil;
import com.tom.utils.ImageUtils;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.atomic.AtomicReference;

public class RecWindows extends AnchorPane {

    private final Rectangle rectangle;
    private final TopBar topBar;
    private String title;

    private Stage stage;

    public RecWindows( Node node, double prefWidth, double prefHeight, double radius ,Stage stage) {
        super();
        this.rectangle = new Rectangle(prefWidth,prefHeight);
        rectangle.setArcWidth(radius);
        rectangle.setArcHeight(radius);
        this.setShape(rectangle);
        this.setPrefSize(prefWidth,prefHeight);
        this.setClip(rectangle);
        this.topBar = new TopBar(this);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(topBar.getTopBar(),node);
        this.getChildren().add(vBox);
        AnchorPaneUtil.setNode(vBox,0.5,0.5,10.0,0.5);
        this.stage = stage;
    }

    public void initStage(){
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
        scene.getStylesheets().add(this.getClass().getResource("/css/myTooltip.css").toExternalForm());
        this.getStyleClass().add("my-windows");
    }


    public void myResize(double prefWidth, double prefHeight){
        rectangle.setWidth(prefWidth);
        rectangle.setHeight(prefHeight);
        this.setPrefSize(prefWidth,prefHeight);
    }

    public void setTitle(String title){
        topBar.setTitle(title);
    }

    /**
     * 窗口最大化/还原处理器
     * @return
     */
    public EventHandler<MouseEvent> maximizedHandler() {
        AtomicReference<Double> nw = new AtomicReference<>(rectangle.getWidth());
        AtomicReference<Double> nh = new AtomicReference<>(rectangle.getHeight());
        System.out.println(STR."nw-\{nw.get()},nh-\{nh.get()}");
        return _ -> {
            if (!stage.isMaximized()) {
                Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
                nw.set(rectangle.getWidth());
                nh.set(rectangle.getHeight());
                this.myResize(visualBounds.getWidth(),visualBounds.getHeight());
                stage.setMaximized(true);
                ImageView restore = ImageUtils.getImageView("/img/restore.png",16,14);
                ObservableList<Node> children = topBar.getMaximizeBox().getChildren();
                children.clear();
                children.add(restore);
            }else {
                this.myResize(nw.get(),nh.get());
                stage.setMaximized(false);
                ImageView restore = ImageUtils.getImageView("/img/maximize.png",16,14);
                ObservableList<Node> children = topBar.getMaximizeBox().getChildren();
                children.clear();
                children.add(restore);
            }
        };
    }


    public EventHandler<MouseEvent> minimizedHandler() {
        return _ -> stage.setIconified(true);
    }
}
