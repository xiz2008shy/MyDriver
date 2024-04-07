package com.tom.pane;

import com.tom.listener.DragListener;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.DrawUtil;
import com.tom.utils.ImageUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RecWindows extends AnchorPane {

    private final Rectangle rectangle;
    private final TopBar topBar;
    private final Stage stage;

    private final VBox showBox = new VBox();

    private final List<Node> tabNodes = new ArrayList<>();

    public <N extends Node & TabWatcher<W>,W>RecWindows( N node, double prefWidth, double prefHeight, double radius ,Stage stage) {
        super();
        this.rectangle = new Rectangle(prefWidth,prefHeight);
        this.topBar = new TopBar<>(this, node);
        this.stage = stage;
        publicCreate(node, prefWidth, prefHeight, radius);
    }


    public RecWindows(Node node, double prefWidth, double prefHeight, double radius ,Stage stage,TabWatcher<?> tabWatcher) {
        super();
        this.rectangle = new Rectangle(prefWidth,prefHeight);
        this.topBar = new TopBar<>(this, tabWatcher);
        this.stage = stage;
        publicCreate(node, prefWidth, prefHeight, radius);
    }


    private void publicCreate(Node node, double prefWidth, double prefHeight, double radius) {
        rectangle.setArcWidth(radius);
        rectangle.setArcHeight(radius);
        this.setShape(rectangle);
        this.setPrefSize(prefWidth, prefHeight);
        this.setClip(rectangle);
        showBox.getChildren().addAll(topBar.getTopBar(), node);
        this.getChildren().add(showBox);
        AnchorPaneUtil.setNode(showBox,0.5,0.5,10.0,0.5);
    }


    /**
     * 务必构造器执行后手动调用该方法
     */
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


    /**
     * 窗口最大化/还原处理器
     * @return
     */
    public EventHandler<MouseEvent> maximizedHandler(Pane pane) {
        AtomicReference<Double> nw = new AtomicReference<>(rectangle.getWidth());
        AtomicReference<Double> nh = new AtomicReference<>(rectangle.getHeight());
        System.out.println(STR."nw-\{nw.get()},nh-\{nh.get()}");
        return e -> {
            System.out.println("maximizedHandler");
            e.consume();
            if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED) && (pane.equals(e.getPickResult().getIntersectedNode())
                || pane.getChildren().get(0).equals(e.getPickResult().getIntersectedNode()))
            ) {
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
            }
        };
    }


    public EventHandler<MouseEvent> minimizedHandler(Pane pane) {
        return e -> {
            e.consume();
            if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED) && (pane.equals(e.getPickResult().getIntersectedNode()) ||
                    pane.getChildren().get(0).equals(e.getPickResult().getIntersectedNode()))) {
                stage.setIconified(true);
            }
        };
    }

    public EventHandler<MouseEvent> closeHandler(Pane pane) {
        return e -> {
            e.consume();
            if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED) && (pane.equals(e.getPickResult().getIntersectedNode())
                    || pane.getChildren().get(0).equals(e.getPickResult().getIntersectedNode()))
            ) {
                Platform.exit();
            }
        };
    }



    public void setActiveNode(int activeIndex){
        if (activeIndex < tabNodes.size()){
            ObservableList<Node> children = this.showBox.getChildren();
            if (children.size() > 1){
                children.remove(1);
            }
            children.add(tabNodes.get(activeIndex));
        }
    }

    void addNodeToTabs(Node tab){
        this.tabNodes.add(tab);
    }

    void removeNodeFromTabs(int index){
        if (index < tabNodes.size()){
            this.tabNodes.remove(index);
        }
    }


    public <T>void createNewTab(TabWatcher<T> tabWatcher, boolean isActive){
        this.topBar.getTabManager().createTab(tabWatcher,isActive,false);
    }

    public <T>void createNewTab(Node node, TabWatcher<T> tabWatcher, boolean isActive){
        this.topBar.getTabManager().createTab(node,tabWatcher,isActive,false);
    }

}
