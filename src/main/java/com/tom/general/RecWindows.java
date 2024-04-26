package com.tom.general;

import com.tom.general.menu.BaseMenu;
import com.tom.handler.DragHandler;
import com.tom.model.ModelData;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.DrawUtil;
import com.tom.utils.ImageUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Slf4j
public class RecWindows extends AnchorPane {

    private final Rectangle rectangle;
    @Getter
    private final TopBar topBar;
    @Getter
    private final Stage stage;

    /**
     * 带顶栏和下面真实节点内容的面板
     */
    @Getter
    private final VBox showBox = new VBox();
    @Getter
    private final StackPane secPane = new StackPane();
    private final Pane block = new Pane();

    /**
     * 多标签下的多真实节点
     * 在RecWindows创建时，通过TabManager将节点添加进集合，随后在 setActiveNode 方法中被添加到showBox的子元素中
     */
    @Getter
    private final List<Node> tabNodes = new ArrayList<>();

    @Getter
    private final List<ModelData> modelDatum = new ArrayList<>();

    /**
     * -- SETTER --
     *  注册面板激活（主要是指node加入recWindows 内部时触发，比如多标签页的情况下，实际会切换不同的node）事件
     */
    @Setter
    private Consumer<RecWindows> whenActive;

    /**
     * 需要跨标签共享的菜单放这里（比如多标签下的右键菜单）
     */
    @Getter @Setter
    private BaseMenu baseMenu;
    @Getter @Setter
    private BaseMenu statusMenu;

    @Getter
    private Node activeNode;

    @Getter
    private ModelData activeModelData;

    /**
     * 最大化/恢复时临时保存位置数据
     */
    private AtomicReference<Double> nw = new AtomicReference<>();
    private AtomicReference<Double> nh = new AtomicReference<>();

    @Setter @Getter
    private RecWindows fromWindows;

    public RecWindows(Node node, double prefWidth, double prefHeight, double radius ,
                      Stage stage, ModelData modelData,int topBarIconFlag) {
        super();
        this.rectangle = new Rectangle(prefWidth,prefHeight);
        this.topBar = new TopBar(this, node,modelData,topBarIconFlag);
        this.stage = stage;
        publicCreate( prefWidth, prefHeight, radius);
    }


    public RecWindows(Node node, double prefWidth, double prefHeight, double radius ,
                      Stage stage, String title,int topBarIconFlag) {
        super();
        this.rectangle = new Rectangle(prefWidth,prefHeight);
        this.topBar = new TopBar(this, title,topBarIconFlag);
        this.stage = stage;
        publicCreate( node,prefWidth, prefHeight, radius);
    }


    /**
     * <scene>
     *     <AnchorPane> - this
     *         <StackPane> - secPane
     *             <VBox> - showBox
     *                 <TopBar>...</TopBar>
     *                 <Node>...</Node> - activeNode
     *             </VBox>
     *             <BaseMenu>...</BaseMenu> - rightClickMenu
     *         </StackPane>
     *     </AnchorPane>
     * </scene>
     * @param prefWidth
     * @param prefHeight
     * @param radius
     */

    private void publicCreate( double prefWidth, double prefHeight, double radius) {
        publicCreate(null,  prefWidth,  prefHeight,  radius);
    }


    private void publicCreate(Node node, double prefWidth, double prefHeight, double radius) {
        rectangle.setArcWidth(radius);
        rectangle.setArcHeight(radius);
        this.setShape(rectangle);
        this.setPrefSize(prefWidth, prefHeight);
        this.setClip(rectangle);
        this.block.setStyle("-fx-background-color: rgba(235,235,235,0.79)");
        secPane.getChildren().add(showBox);
        showBox.getChildren().add(topBar);
        if (node != null){
            showBox.getChildren().add(node);
        }
        this.getChildren().add(secPane);
        AnchorPaneUtil.setNode(secPane,0.5,0.5,0.5,0.5);
    }


    /**
     * 务必构造器执行后手动调用该方法
     */
    public void initStage(){
        if (fromWindows != null) {
            this.fromWindows.setInActiveStyle();
        }
        setActiveNode(0);
        Scene scene = new Scene(this);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setWidth(rectangle.getWidth());
        stage.setHeight(rectangle.getHeight());
        // 添加窗体拉伸效果
        DrawUtil.addDrawFunc(stage, this);
        DragHandler dragListener = new DragHandler(this);
        topBar.addEventHandler(MouseEvent.MOUSE_PRESSED,dragListener);
        topBar.addEventHandler(MouseEvent.MOUSE_DRAGGED,dragListener);
        topBar.addEventHandler(MouseEvent.MOUSE_CLICKED,dragListener::clickHandle);
        scene.getStylesheets().add(this.getClass().getResource("/css/rec_windows.css").toExternalForm());
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
        log.info(STR."nw-\{nw.get()},nh-\{nh.get()}");
        return e -> {
            log.info("maximizedHandler");
            e.consume();
            if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED) && (pane.equals(e.getPickResult().getIntersectedNode())
                || pane.getChildren().get(0).equals(e.getPickResult().getIntersectedNode()))
            ) {
                this.getBaseMenu().closeMenu(null);
                this.getStatusMenu().closeMenu(null);
                RecWindows.maximizedOrRestore(this);
            }
        };
    }

    public static void maximizedOrRestore(RecWindows recWindows) {
        if (!recWindows.stage.isMaximized()) {
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            recWindows.nw.set(recWindows.rectangle.getWidth());
            recWindows.nh.set(recWindows.rectangle.getHeight());
            recWindows.myResize(visualBounds.getWidth(),visualBounds.getHeight());
            recWindows.stage.setMaximized(true);
            ImageView restore = ImageUtils.getImageView("/img/restore.png",16,14);
            ObservableList<Node> children = recWindows.topBar.getMaximizeBox().getChildren();
            children.clear();
            children.add(restore);
        }else {
            recWindows.myResize(recWindows.nw.get(), recWindows.nh.get());
            recWindows.stage.setMaximized(false);
            ImageView restore = ImageUtils.getImageView("/img/maximize.png",16,14);
            ObservableList<Node> children = recWindows.topBar.getMaximizeBox().getChildren();
            children.clear();
            children.add(restore);
        }
    }


    public EventHandler<MouseEvent> minimizedHandler(Pane pane) {
        return e -> {
            e.consume();
            if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED) && (pane.equals(e.getPickResult().getIntersectedNode()) ||
                    pane.getChildren().getFirst().equals(e.getPickResult().getIntersectedNode()))) {
                this.getBaseMenu().closeMenu(null);
                this.getStatusMenu().closeMenu(null);
                stage.setIconified(true);
            }
        };
    }

    public EventHandler<MouseEvent> closeHandler(Pane pane) {
        return e -> {
            e.consume();
            if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED) && (pane.equals(e.getPickResult().getIntersectedNode())
                    || pane.getChildren().getFirst().equals(e.getPickResult().getIntersectedNode()))
            ) {
                close();
            }
        };
    }

    public void close() {
        if (this.getFromWindows() == null) {
            Platform.exit();
        }else {
            this.getFromWindows().setActiveStyle();
            this.stage.close();
        }
    }


    public void setActiveNode(int activeIndex){
        if (activeIndex < tabNodes.size()){
            Node node = tabNodes.get(activeIndex);
            ModelData modelData = modelDatum.get(activeIndex);
            this.topBar.setActiveIndex(activeIndex);
            if (!node.equals(activeNode)) {
                ObservableList<Node> children = this.showBox.getChildren();
                if (children.size() > 1 ){
                    children.remove(1);
                }
                this.activeNode = node;
                this.activeModelData = modelData;
                children.add(activeNode);
                // 替换完内部节点后，执行whenActive方法
                if (whenActive != null){
                    whenActive.accept(this);
                }
            }
        }
    }

    void addNodeToWindows(Node tab,ModelData modelData){
        this.tabNodes.add(tab);
        this.modelDatum.add(modelData);
    }

    void removeNodeFromTabs(int index){
        if (index < tabNodes.size()){
            this.tabNodes.remove(index);
            this.modelDatum.remove(index);
        }
    }


    public void createNewTab(Node node,ModelData modelData, boolean isActive){
        this.topBar.getTabManager().createTab(node,modelData,isActive,false);
    }

    /**
     * 关闭未激活的标签，当前激活标签不动
     */
    public void closeInActiveTab(){
        int activeIndex = this.getTopBar().getActiveIndex();
        List<Node> nodes = this.getTabNodes();
        List<ModelData> models = this.getModelDatum();
        ObservableList<Node> tabChildren = this.getTopBar().getTabManager().getChildren();
        if (activeIndex == 0){
            int times = nodes.size() - 1;
            for (int i = 0; i < times; i++) {
                closeLastTabWithoutActiveOtherTab(nodes, tabChildren, models);
            }
        }else if (activeIndex == nodes.size() - 1){
            int times = nodes.size() - 1;
            for (int i = 0; i < times; i++) {
                closeFirstTabWithoutActiveOtherTab(nodes, tabChildren, models);
            }
        }else {
            int rTimes = nodes.size() - (activeIndex + 1);
            for (int i = 0; i < rTimes; i++) {
                closeLastTabWithoutActiveOtherTab(nodes, tabChildren, models);
            }
            int lTimes = nodes.size() - 1;
            for (int i = 0; i < lTimes; i++) {
                closeFirstTabWithoutActiveOtherTab(nodes, tabChildren, models);
            }
        }
        Node node = tabChildren.getFirst();
        HBox.setMargin(node,new Insets(0,0,0,0));
        this.getTopBar().setActiveIndex(0);
    }

    private static void closeFirstTabWithoutActiveOtherTab(List<Node> nodes, ObservableList<Node> tabChildren, List<ModelData> models) {
        nodes.removeFirst();
        tabChildren.removeFirst();
        models.removeFirst();
    }

    private static void closeLastTabWithoutActiveOtherTab(List<Node> nodes, ObservableList<Node> tabChildren, List<ModelData> models) {
        nodes.removeLast();
        tabChildren.removeLast();
        models.removeLast();
    }


    public void setActiveStyle(){
        this.secPane.getChildren().remove(block);
    }

    public void setInActiveStyle(){
        this.secPane.getChildren().add(block);
    }
}
