package com.tom.general;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TopBar<T> extends AnchorPane{

    private final TabManager tabManager;

    private HBox closeBox;

    private HBox maximizeBox;

    private HBox minimizeBox;

    private Pane activeTab = null;
    private int activeIndex = 0;


    /**
     * <TopBar>
     *     <TabManager> * tabs @see com.tom.general.TabManager#doCreateTab </TabManager>
     *     <HBox> -rightIcons
     *         <HBox> -gear
     *             <ImageView></ImageView>
     *         </HBox>
     *         <HBox> -minimize
     *              <ImageView></ImageView>
     *         </HBox>
     *         ...
     *     </HBox>
     * </TopBar>
     * @param recWindows
     * @param tabWatcher
     */
    public TopBar(RecWindows recWindows, TabWatcher<T> tabWatcher) {
        this.tabManager = new TabManager(recWindows,this);
        if (tabWatcher != null) {
            tabManager.createTab(tabWatcher,true,true);
        }
        initTopBar(recWindows);
    }


    public TopBar(RecWindows recWindows, Node node ,TabWatcher<T> tabWatcher) {
        this.tabManager = new TabManager(recWindows,this);
        if (tabWatcher != null) {
            tabManager.createTab(node,tabWatcher,true,true);
        }
        initTopBar(recWindows);
    }


    private void initTopBar(RecWindows recWindows) {
        this.setPrefHeight(25);
        this.setStyle("-fx-background-color: rgb(216, 218, 219)");
        HBox rightIcons = createRightPart(recWindows);
        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);
        this.getChildren().addAll(tabManager,rightIcons);
        AnchorPaneUtil.setNode(tabManager,7.0,160.0,0.0, 0.0);
    }


    private HBox createRightPart(RecWindows recWindows) {
        HBox rightIcons = new HBox();
        rightIcons.setPrefWidth(152);

        EventHandler<MouseEvent> blueMoveOnHandler = getBlueOrRedMoveOnHandler(true,null,null);
        HBox h1 = createSquareIconBox("/img/gear.png", 32,20, 9,blueMoveOnHandler);

        this.minimizeBox = createSquareIconBox("/img/minimize.png", 32,14, 12,blueMoveOnHandler);
        EventHandler<MouseEvent> minimizedHandler = recWindows.minimizedHandler(minimizeBox);
        minimizeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,minimizedHandler);
        minimizeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,minimizedHandler);

        this.maximizeBox = createSquareIconBox("/img/maximize.png", 16,14, 12,blueMoveOnHandler);
        EventHandler<MouseEvent> maximizedHandler = recWindows.maximizedHandler(maximizeBox);
        maximizeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,maximizedHandler);
        maximizeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,maximizedHandler);

        this.closeBox = createSquareIconBox("/img/close.png", 16,14, 12,null);

        ImageView normal = (ImageView)closeBox.getChildren().getFirst();
        ImageView closed = ImageUtils.getImageView("/img/close-white.png",16,14);
        EventHandler<MouseEvent> redMoveOnHandler = getBlueOrRedMoveOnHandler(false,normal,closed);
        setMoveOnHandler(redMoveOnHandler, closeBox);
        EventHandler<MouseEvent> closeHandler = recWindows.closeHandler(closeBox);
        closeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,closeHandler);
        closeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,closeHandler);

        rightIcons.getChildren().addAll(h1,minimizeBox,maximizeBox,closeBox);
        return rightIcons;
    }

    private static HBox createSquareIconBox(String image, int size, int resize,int padding,EventHandler<MouseEvent> moveOnHandler) {
        ImageView closeIcon = ImageUtils.getImageView(image, size, resize);
        HBox hbox = new HBox(closeIcon);
        hbox.setPadding(new Insets(padding));
        setMoveOnHandler(moveOnHandler, hbox);
        return hbox;
    }

    private static void setMoveOnHandler(EventHandler<MouseEvent> moveOnHandler, HBox hbox) {
        if (moveOnHandler != null) {
            hbox.setOnMouseEntered(moveOnHandler);
            hbox.setOnMouseExited(moveOnHandler);
        }
    }


    public <R extends Pane>EventHandler<MouseEvent> getBlueOrRedMoveOnHandler(boolean blueOrRed,ImageView normal,ImageView closed) {
        final boolean[] flag = {true};
        final String colorStyle = blueOrRed ? "-fx-background-color: #dce9f1" : "-fx-background-color: rgb(196, 43, 28)";
        return  e -> {
            Pane source = (R)e.getSource();
            if (flag[0]) {
                source.setStyle(colorStyle);
                if (!blueOrRed){
                    ObservableList<Node> children = source.getChildren();
                    children.clear();
                    children.add(closed);
                }
            } else {
                source.setStyle("-fx-background-color: none");
                if (!blueOrRed){
                    ObservableList<Node> children = source.getChildren();
                    children.clear();
                    children.add(normal);
                }
            }
            flag[0] = !flag[0];
        };
    }


    public HBox getCloseBox() {
        return closeBox;
    }

    public HBox getMaximizeBox() {
        return maximizeBox;
    }

    public TabManager getTabManager() {
        return tabManager;
    }

    public Pane getActiveTab() {
        return activeTab;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveTab(Pane activeTab) {
        this.activeTab = activeTab;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }
}
