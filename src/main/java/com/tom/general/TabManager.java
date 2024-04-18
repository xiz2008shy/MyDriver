package com.tom.general;

import com.tom.model.ModelData;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Shape;


/**
 * 标签切卡管理器(标签切卡的存放容器)
 */
public class TabManager extends HBox {

    public static final Shape ACTIVE_SHARP = HeadTabShape.headTabShape(260, 35, 7);
    public static final Shape INACTIVE_SHARP = HeadTabShape.headTabSecShape(260, 35, 7);

    private final RecWindows recWindows;

    private final TopBar topBar;

    private final String title;


    public TabManager(String title) {
        this.title = title;
        this.topBar = null;
        this.recWindows = null;
        Label label = new Label(this.title);
        this.getChildren().add(label);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-font-size: 14;");
        HBox.setMargin(label,new Insets(-5,0,0,20));
    }


    public TabManager(RecWindows recWindows,TopBar topBar) {
        this.recWindows = recWindows;
        this.topBar = topBar;
        this.title = null;
    }


    public <T>void createTab(Node node, ModelData modelData, boolean isActive,boolean isFirst) {
        int index = doCreateTab(modelData, isActive);
        recWindows.addNodeToWindows(node,modelData);
        if (isActive && !isFirst) {
            recWindows.setActiveNode(index);
        }
    }


    /**
     * <AnchorPane> - tab
     *     <ImageView></ImageView> -icon
     *     <HBox>
     *         <Label></Label> -title
     *     </HBox>
     *     <HBox> -close
     *         <ImageView></ImageView> -closeIcon
     *     </HBox>
     * </AnchorPane>
     * @param modelData
     * @param isActive
     * @return
     */
    private int doCreateTab(ModelData modelData, boolean isActive) {
        AnchorPane ap = new AnchorPane();
        ap.getStyleClass().add("my_tabs");
        if (isActive){
            switchTab(ap);
        }else {
            ap.setShape(INACTIVE_SHARP);
        }

        ap.setPrefSize(260,35);
        ImageView imageView = ImageUtils.getImageView("/img/fileDir32.png", 19, 19);
        Label label = new Label();
        String initTitle = modelData.getCurDirProperty().get().getName();
        label.setText(initTitle);
        modelData.getCurDirProperty().addListener((_, old, newValue) -> {
            label.setText(newValue.getName());
        });

        HBox textBox = new HBox(label);
        textBox.getStyleClass().add("my_tabs_hbox");

        ImageView closeIcon = ImageUtils.getImageView("/img/close.png", 16, 12);
        HBox close = new HBox(closeIcon);

        close.getStyleClass().add("my_tabs_close");
        ap.getChildren().addAll(imageView,textBox,close);
        AnchorPaneUtil.setNode(imageView,5.0,null,0.0, 15.0);
        AnchorPaneUtil.setNode(textBox,0.0,30.0,0.0, 45.0);
        AnchorPaneUtil.setNode(close,6.0,20.0,5.0, null);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        ObservableList<Node> children = this.getChildren();
        children.add(ap);
        int size = children.size();

        if(size > 1){
            HBox.setMargin(ap,new Insets(0,0,0,-7));
        }

        ap.addEventHandler(MouseEvent.MOUSE_DRAGGED, Event::consume);
        ap.addEventHandler(MouseEvent.MOUSE_CLICKED,e -> {
            e.consume();
            if (e.getButton().equals(MouseButton.PRIMARY)){
                if (!ap.equals(this.recWindows.getTopBar().getActiveTab())) {
                    switchTab(ap);
                    recWindows.setActiveNode(this.getChildren().indexOf(ap));
                }else if (e.getClickCount() == 2){
                    closeTab(ap);
                }
            }
        });
        close.addEventHandler(MouseEvent.MOUSE_CLICKED,e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)){
                e.consume();
                closeTab(ap);
            }
        });

        return size - 1;
    }

    private void closeTab(AnchorPane ap) {
        ObservableList<Node> list = this.getChildren();
        int nodeIndex = list.indexOf(ap);
        this.recWindows.removeNodeFromTabs(nodeIndex);
        list.remove(nodeIndex);
        if (nodeIndex > 0){
            int i = nodeIndex - 1;
            closeAndActiveBesideTab(list, i);
        }else if(!list.isEmpty()){
            closeAndActiveBesideTab(list, nodeIndex);
        }else {
            Platform.exit();
        }
    }

    private void closeAndActiveBesideTab(ObservableList<Node> list, int i) {
        Pane pane = (Pane) list.get(i);
        if (i == 0) {
            HBox.setMargin(pane,new Insets(0,0,0,0));
        }
        switchTab(pane);
        this.recWindows.setActiveNode(i);
    }

    public void switchTab(Pane ap) {
        Pane curActiveNode = topBar.getActiveTab();
        if (curActiveNode != null){
            curActiveNode.setShape(INACTIVE_SHARP);
            curActiveNode.getStyleClass().remove("my_tabs_active");
        }
        ap.setShape(ACTIVE_SHARP);
        ap.getStyleClass().add("my_tabs_active");
        topBar.setActiveTab(ap);
    }


}
