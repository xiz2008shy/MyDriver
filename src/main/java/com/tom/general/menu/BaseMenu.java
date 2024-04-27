package com.tom.general.menu;

import com.tom.general.RecWindows;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Consumer;

@Slf4j
public class BaseMenu extends StackPane{

    private final ImageView bg;

    @Getter
    private final VBox menuContent;

    /**
     * 单个菜单的高度
     */
    private final double preHeight;

    /**
     * 菜单整体宽度
     */
    private int intWidth;
    /**
     * 菜单整体高度
     */
    private int intHeight;

    private final Rectangle rectangle;

    @Getter
    private final HBox realPane = new HBox();

    @Getter
    private Consumer<BaseMenu> closeMenuHandler;

    @Getter
    private final RecWindows menuBindWindow;

    private ShowMenu showMenu;


    /**
     * <StackPane> - secPane (recWindows)
     *     <HBox> - realPane(this.realPane)
     *         <StackPane> - this
     *             <ImageView></ImageView> - background
     *             <VBox> - menuContent(this)
     *                 <HBox> - MyMenuContent
     *                      <Label></Label>
     *                 </HBox>
     *             </VBox>
     *         </StackPane>
     *     </HBox>
     * </StackPane>
     * @param width
     * @param preHeight
     * @param windows
     */
    public <T extends ShowMenu<?>>BaseMenu(double width, double preHeight, RecWindows windows, Class<T> clazz) {
        super();
        this.setShowMenuClazz(clazz);
        this.menuBindWindow = windows;
        this.preHeight = preHeight;
        this.intHeight = (int)preHeight;
        this.intWidth = (int)width;
        this.showMenu.setMenuWidth(intWidth);
        this.showMenu.setMenuHeight(intHeight);
        this.rectangle = new Rectangle(intWidth,intHeight);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        myResize(intWidth,intHeight);
        realPane.getChildren().add(this);
        DropShadow dropShadow = new DropShadow();
        // 设置阴影的模糊半径
        dropShadow.setRadius(5.0);
        // 设置阴影的水平偏移
        dropShadow.setOffsetX(3.0);
        // 设置阴影的垂直偏移
        dropShadow.setOffsetY(3.0);
        // 设置阴影的颜色
        dropShadow.setColor(Color.GRAY);
        // 设置阴影的宽度（即阴影的尺寸）
        dropShadow.setWidth(10.0);
        //dropShadow.setSpread(0.5);
        realPane.setEffect(dropShadow);
        this.bg = new ImageView();
        bg.setEffect(new GaussianBlur(30));
        this.menuContent = new VBox();
        menuContent.setPrefSize(width,preHeight);
        menuContent.setStyle("-fx-background-color: rgba(251, 251, 252,0.75);");
        this.getChildren().add(bg);
        this.getChildren().add(menuContent);
        windows.getSecPane().getChildren().add(this.realPane);
        this.realPane.setVisible(false);
    }

    private void myResize(double width,double height) {
        this.intWidth = (int)width;
        this.intHeight = (int)height;
        this.rectangle.setWidth(width);
        this.rectangle.setHeight(height);
        this.showMenu.setMenuWidth(width);
        this.showMenu.setMenuHeight(height);
        this.realPane.setMaxWidth(width);
        this.realPane.setMaxHeight(height);
        this.setPrefSize(width,height);
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        this.setShape(rectangle);
        this.setClip(rectangle);
    }

    public void setMenuBg(Pane pane,double x,double y){
        WritableImage writableImage = new WritableImage(intWidth, intHeight);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(x,y,intWidth,intHeight));
        pane.snapshot(snapshotParameters,writableImage);
        bg.setImage(writableImage);
    }

    public void addMenuContent(MyMenuContext myMenuContext){
        ObservableList<Node> children = menuContent.getChildren();
        children.add(myMenuContext);
        VBox.setVgrow(myMenuContext, Priority.ALWAYS);
        this.myResize(intWidth, this.menuContent.getChildren().size() * preHeight);
        myMenuContext.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            e.consume();
            myMenuContext.getMouseActiveHandler().handle(e);
            this.closeMenu(null);
        });
    }


    public <T extends ShowMenu<?>>void setShowMenuClazz(Class<T> clazz){
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            MethodHandle constructor = lookup.findConstructor(clazz, MethodType.methodType(void.class));
            this.showMenu = (T)constructor.invoke();
        } catch (Throwable e) {
            log.error("BaseMenu.setShowMenuClazz occurred an error,cause:",e);
        }
    }


    public boolean isShow() {
        return this.realPane.isVisible();
    }

    public void showMenu(Event e) {
        if (e instanceof MouseEvent event){
            this.setMenuBg(menuBindWindow.getShowBox(),event.getSceneX(),event.getSceneY());
            for (Node child : this.getMenuContent().getChildren()) {
                MyMenuContext menuContext = (MyMenuContext) child;
                menuContext.getStyleClass().remove("my_disabled");
                if (menuContext.getVisiblePredicate().test(event)){
                    menuContext.setVisible(true);
                    if (menuContext.getDisabledPredicate().test(event)) {
                        menuContext.getStyleClass().add("my_disabled");
                        menuContext.setDisable(true);
                    }else {
                        menuContext.setDisable(false);
                    }
                }else {
                    menuContext.setVisible(false);
                }
            }
            this.showMenu.showMenu(event,this);
        }
    }

    public void closeMenu(Event e){
        this.showMenu.closeMenu(e,this);
    }
}
