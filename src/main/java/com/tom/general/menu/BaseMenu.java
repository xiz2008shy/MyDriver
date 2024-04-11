package com.tom.general.menu;

import com.tom.general.RecWindows;
import com.tom.utils.DeliverUtils;
import javafx.collections.ObservableList;
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

import java.util.function.Consumer;

public class BaseMenu extends StackPane{

    private final ImageView bg;

    private final VBox menuContent;

    private final double myWidth;
    private final int intWidth;

    private double preHeight;
    private double myHeight;
    private int intHeight;

    private Rectangle rectangle;

    private final HBox realPane = new HBox();

    private double myTransX = 0;
    private double myTransY = 0;

    private Consumer<BaseMenu> closeMenuHandler;


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
    public BaseMenu(double width,double preHeight, RecWindows windows) {
        super();
        this.myWidth = width;
        this.myHeight = preHeight;
        this.preHeight = preHeight;
        this.intWidth = (int)width;
        this.intHeight = (int)preHeight;
        this.rectangle = new Rectangle(width,myHeight);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        myResize(width,myHeight);
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
        menuContent.setPrefSize(width,myHeight);
        menuContent.setStyle("-fx-background-color: rgba(251, 251, 252,0.75);");
        this.getChildren().add(bg);
        this.getChildren().add(menuContent);
        windows.getSecPane().getChildren().add(this.realPane);
        this.realPane.setVisible(false);
        DeliverUtils.setBaseMenu(this);
    }

    private void myResize(double width,double height) {
        this.myHeight = height;
        this.intHeight = (int)height;
        this.rectangle.setWidth(width);
        this.rectangle.setHeight(height);
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
        snapshotParameters.setViewport(new Rectangle2D(x,y,myWidth,myHeight));
        pane.snapshot(snapshotParameters,writableImage);
        bg.setImage(writableImage);
    }

    public void addMenuContent(MyMenuContext myMenuContext){
        ObservableList<Node> children = menuContent.getChildren();
        children.add(myMenuContext);
        VBox.setVgrow(myMenuContext, Priority.ALWAYS);
        this.myResize(myWidth, this.menuContent.getChildren().size() * preHeight);
        myMenuContext.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            myMenuContext.getMouseActiveHandler().handle(e);
            this.closeMenu();
        });

    }


    public void showMenu( MouseEvent event,RecWindows pane){
        double originX ;
        double originY ;
        double maxWWhI = pane.getWidth() - myWidth;
        double halfW = (int)pane.getWidth() >> 1;
        double maxHWhI = pane.getHeight() - myHeight;
        double halfH = (int)pane.getHeight() >> 1;
        int menuHalfWidth = intWidth >> 1;
        int menuHalfHeight = intHeight >> 1;
        /**
         * 这里根据当前鼠标点击位置坐标和原菜单面版的 宽度/高度比较 确定选取菜单面板四角中的一角坐标作为计算原点
         */
        if (event.getSceneX() < maxWWhI){
            if (event.getSceneY() < maxHWhI) {
                originX = halfW - menuHalfWidth;
                originY = halfH - menuHalfHeight;
            }else {
                originX = halfW - menuHalfWidth;
                originY = halfH + menuHalfHeight;
            }
        }else {
            if (event.getSceneY() < maxHWhI) {
                originX = halfW + menuHalfWidth;
                originY = halfH - menuHalfHeight;
            }else {
                originX = halfW + menuHalfWidth;
                originY = halfH + menuHalfHeight;
            }
        }


        myTransX =  event.getSceneX() - originX;
        myTransY =  event.getSceneY() - originY;

        this.realPane.setTranslateX(myTransX);
        this.realPane.setTranslateY(myTransY);
        this.setMenuBg(pane.getShowBox(),event.getSceneX(),event.getSceneY());
        for (Node child : this.menuContent.getChildren()) {
            MyMenuContext menuContext = (MyMenuContext) child;
            if (menuContext.getVisiblePredicate().test(event)){
                menuContext.setVisible(true);
                if (menuContext.getDisabledPredicate().test(event)) {
                    menuContext.getStyleClass().add("my_disabled");
                    menuContext.setDisable(true);
                }else {
                    menuContext.getStyleClass().remove("my_disabled");
                    menuContext.setDisable(false);
                }
            }else {
                menuContext.setVisible(false);
            }
        }

        this.realPane.setVisible(true);
    }

    public void closeMenu(){
        this.realPane.setTranslateX(-myTransX);
        this.realPane.setTranslateY(-myTransY);
        this.myTransX = 0 ;
        this.myTransY = 0 ;
        this.realPane.setVisible(false);
        if (closeMenuHandler != null){
            closeMenuHandler.accept(this);
        }
    }


    public boolean isShow() {
        return this.realPane.isVisible();
    }

    public void setCloseMenuHandler(Consumer<BaseMenu> closeMenuHandler) {
        this.closeMenuHandler = closeMenuHandler;
    }

    public VBox getMenuContent() {
        return menuContent;
    }
}
