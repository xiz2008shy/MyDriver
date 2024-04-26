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
import lombok.Setter;

import java.util.function.Consumer;

public class BaseMenu<T extends ShowMenu<R>,R extends Event> extends StackPane{

    private final ImageView bg;

    @Getter
    private final VBox menuContent;

    private double preHeight;

    private int intWidth;
    private int intHeight;

    private Rectangle rectangle;

    @Getter
    private final HBox realPane = new HBox();


    private Consumer<BaseMenu> closeMenuHandler;

    @Getter
    private final RecWindows menuBindWindow;
    @Setter
    private Class<T> showMenuClazz;

    private ShowMenu<R> showMenu;


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
        this.menuBindWindow = windows;
        this.preHeight = preHeight;
        this.intHeight = (int)preHeight;
        this.intWidth = (int)width;
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
            this.closeMenu();
        });
    }




    public void setShowMenu( MouseEvent event,RecWindows pane){

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

}
