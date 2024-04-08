package com.tom.menu;

import com.tom.pane.RecWindows;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class BaseMenu extends StackPane{

    private ImageView bg;

    private VBox menuContent;

    double width;

    double height;

    Translate transform = new Translate();

    public BaseMenu(double width, double height,RecWindows pane) {
        super();
        this.width = width;
        this.height = height;
        this.setPrefSize(width,height);
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        AnchorPane background = new AnchorPane();
        background.setPrefSize(width,height);
        background.setStyle("-fx-background-radius: 20;");
        background.setPrefSize(width,height);
        this.bg = new ImageView();
        //bg.setEffect(new GaussianBlur(20));
        background.getChildren().add(bg);
        this.menuContent = new VBox();
        menuContent.setPrefSize(width,height);
        menuContent.setStyle("-fx-background-color: rgba(255,255,255,0.55);-fx-background-radius: 20;");
        addMenuContent();
        this.getChildren().add(background);
        this.getChildren().add(menuContent);
        pane.getSecPane().getChildren().add(this);
        this.addMenuContent();
        this.setVisible(false);
    }

    public void setMenuBg(Pane pane){
        WritableImage writableImage = new WritableImage((int)width, (int)height);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setTransform(transform);
        pane.snapshot(snapshotParameters,writableImage);
        bg.setImage(writableImage);
    }

    public void addMenuContent(){
        ObservableList<Node> children = menuContent.getChildren();
        children.clear();
        for (int i = 0; i < 4; i++) {
            children.add(new Text(STR."test_\{i}"));
        }
    }

    public void showMenu( MouseEvent event,RecWindows pane){
        System.out.println("showMenu");
        double paneWidth = pane.getWidth() / 2 ;
        double paneHeight = pane.getHeight() / 2;
        double xTrans = event.getSceneX() - paneWidth;
        double yTrans = event.getSceneY() - paneHeight;
        this.setTranslateX(xTrans);
        this.setTranslateY(yTrans);
        transform.transform(event.getSceneX(),event.getSceneY());
        this.setMenuBg(pane);
        this.setVisible(true);
    }

}
