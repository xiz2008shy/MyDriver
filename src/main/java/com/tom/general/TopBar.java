package com.tom.general;

import com.tom.config.MySetting;
import com.tom.model.ModelData;
import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class TopBar extends AnchorPane{

    // CL-关闭 MX-最大化 MI-最小化 CU-自定义1 CU-自定义2
    private static final int CL_B_I = 1;
    private static final int MX_B_I = 2;
    private static final int MI_B_I = 4;
    private static final int CU_B_I = 8;
    private static final int CU_B_S = 16;

    @Getter
    private final TabManager tabManager;
    @Getter
    private StatusBar statusBar;

    @Getter
    private HBox closeBox;

    @Getter
    private HBox maximizeBox;
    @Getter
    private HBox minimizeBox;

    @Setter
    @Getter
    private Pane activeTab = null;
    @Setter
    @Getter
    private int activeIndex = 0;

    /**
     * 用于控制顶栏右侧的4个按钮栏位是否展示，单个按钮值对应8、4、2、1
     */
    private final int topBarIconFlag;
    /**
     * 需要顶栏带切卡时的构造器
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
     * @param node
     * @param modelData
     */
    public TopBar(RecWindows recWindows, Node node , ModelData modelData,int topBarIconFlag) {
        this.topBarIconFlag = topBarIconFlag;
        this.tabManager = new TabManager(recWindows,this);
        if (modelData != null) {
            tabManager.createTab(node,modelData,true,true);
        }
        initTopBar(recWindows,topBarIconFlag);
    }


    /**
     * 不需要切卡的调用的构造器
     * @param recWindows
     * @param title
     */
    public TopBar(RecWindows recWindows , String title,int topBarIconFlag) {
        this.tabManager = new TabManager(title);
        this.topBarIconFlag = topBarIconFlag;
        initTopBar(recWindows,topBarIconFlag);
    }


    private void initTopBar(RecWindows recWindows,int topBarIconFlag) {
        this.setPrefHeight(25);
        this.getStyleClass().add("top_bar_normal");
        HBox rightIcons = createRightPart(recWindows);

        this.getChildren().addAll(tabManager,rightIcons);
        // 判断是否展示状态栏
        if ((topBarIconFlag & CU_B_S) > 0){
            this.statusBar = new StatusBar(recWindows);
            this.getChildren().add(statusBar);
            AnchorPaneUtil.setNode(statusBar,3.0,160.0,5.0, null);
        }

        AnchorPaneUtil.setNode(rightIcons,0.0,0.0,0.0, null);
        AnchorPaneUtil.setNode(tabManager,7.0,230.0,0.0, 0.0);
    }


    private HBox createRightPart(RecWindows recWindows) {
        HBox rightIcons = new HBox();
        rightIcons.setPrefWidth(152);
        rightIcons.setAlignment(Pos.CENTER_RIGHT);
        ObservableList<Node> children = rightIcons.getChildren();
        EventHandler<MouseEvent> blueMoveOnHandler = getBlueOrRedMoveOnHandler(true,null,null);

        if ((topBarIconFlag & CU_B_I) != 0){
            HBox gear = createSquareIconBox("/img/gear.png", 32,20, 9,blueMoveOnHandler);
            gear.addEventHandler(MouseEvent.MOUSE_RELEASED, MySetting.openSettingPane(gear,recWindows));
            children.add(gear);
        }

        if ((topBarIconFlag & MI_B_I) != 0){
            this.minimizeBox = createSquareIconBox("/img/minimize.png", 32,14, 12,blueMoveOnHandler);
            EventHandler<MouseEvent> minimizedHandler = recWindows.minimizedHandler(minimizeBox);
            minimizeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,minimizedHandler);
            minimizeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,minimizedHandler);
            children.add(minimizeBox);
        }

        if ((topBarIconFlag & MX_B_I) != 0){
            this.maximizeBox = createSquareIconBox("/img/maximize.png", 16,14, 12,blueMoveOnHandler);
            EventHandler<MouseEvent> maximizedHandler = recWindows.maximizedHandler(maximizeBox);
            maximizeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,maximizedHandler);
            maximizeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,maximizedHandler);
            children.add(maximizeBox);
        }

        if ((topBarIconFlag & CL_B_I) != 0){
            this.closeBox = createSquareIconBox("/img/close.png", 16,14, 12,null);
            ImageView normal = (ImageView)closeBox.getChildren().getFirst();
            ImageView closed = ImageUtils.getImageView("/img/close-white.png",16,14);
            EventHandler<MouseEvent> redMoveOnHandler = getBlueOrRedMoveOnHandler(false,normal,closed);
            setMoveOnHandler(redMoveOnHandler, closeBox);
            EventHandler<MouseEvent> closeHandler = recWindows.closeHandler(closeBox);
            closeBox.addEventHandler(MouseEvent.MOUSE_RELEASED,closeHandler);
            closeBox.addEventHandler(MouseEvent.MOUSE_DRAGGED,closeHandler);
            children.add(closeBox);
        }

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

}
