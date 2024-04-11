package com.tom.general;

import com.tom.utils.AnchorPaneUtil;
import com.tom.utils.ImageUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Shape;


/**
 * 标签切卡管理器
 */
public class TabManager {

    public static final Shape ACTIVE_SHARP = HeadTabShape.headTabShape(260, 35, 7);
    public static final Shape INACTIVE_SHARP = HeadTabShape.headTabSecShape(260, 35, 7);

    private HBox tabs = new HBox();

    private RecWindows recWindows;

    private IntegerProperty activeProperty = new SimpleIntegerProperty(0);

    private AnchorPane activeTab = null;

    public TabManager(RecWindows recWindows) {
        this.recWindows = recWindows;
    }

    public <T>void createTab(TabWatcher<T> tabWatcher, boolean isActive,boolean isFirst) {
        if (tabWatcher instanceof Node){
            int index = doCreateTab(tabWatcher, isActive);
            recWindows.addNodeToTabs((Node)tabWatcher);
            if (isActive && !isFirst) {
                recWindows.setActiveNode(index);
            }

        }else {
            throw new RuntimeException("TabManager.createTab is called by error,please check again!");
        }
    }

    public <T>void createTab(Node node, TabWatcher<T> tabWatcher, boolean isActive,boolean isFirst) {
        int index = doCreateTab(tabWatcher, isActive);
        recWindows.addNodeToTabs(node);
        if (isActive && !isFirst) {
            recWindows.setActiveNode(index);
        }
    }


    /**
     * <AnchorPane>
     *     <ImageView></ImageView> -icon
     *     <HBox>
     *         <Label></Label> -title
     *     </HBox>
     *     <ImageView></ImageView> -close
     * </AnchorPane>
     * @param tabWatcher
     * @param isActive
     * @return
     */
    private int doCreateTab(TabWatcher tabWatcher, boolean isActive) {
        AnchorPane ap = new AnchorPane();
        ap.getStyleClass().add("my_tabs");
        if (isActive){
            ap.setShape(ACTIVE_SHARP);
            ap.getStyleClass().add("my_tabs_active");
            this.activeTab = ap;
        }else {
            ap.setShape(INACTIVE_SHARP);
        }

        ap.setPrefSize(260,35);
        ImageView imageView = ImageUtils.getImageView("/img/fileDir32.png", 19, 19);
        Label label = new Label();
        String initTitle = tabWatcher.getInitTitle();
        label.setText(initTitle);
        tabWatcher.getWatcher().addListener((_, oldValue, newValue) -> {
            String title = tabWatcher.refreshTitle(oldValue, newValue);
            label.setText(title);
        });

        HBox textBox = new HBox(label);
        textBox.getStyleClass().add("my_tabs_hbox");

        ImageView close = ImageUtils.getImageView("/img/close.png", 16, 12);
        ap.getChildren().addAll(imageView,textBox,close);
        AnchorPaneUtil.setNode(imageView,5.0,null,0.0, 15.0);
        AnchorPaneUtil.setNode(textBox,0.0,25.0,0.0, 45.0);
        AnchorPaneUtil.setNode(close,6.0,15.0,0.0, null);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        int size = tabs.getChildren().size();
        IntegerProperty curIndex = new SimpleIntegerProperty(size);
        if (isActive){
            this.activeProperty.set(curIndex.get());
        }

        if(size != 0){
            HBox.setMargin(ap,new Insets(0,0,0,-7));
        }

        ap.addEventHandler(MouseEvent.MOUSE_DRAGGED, Event::consume);
        ap.addEventHandler(MouseEvent.MOUSE_CLICKED,_ -> {
            if (curIndex.get() != activeProperty.get()) {
                activeTab.getStyleClass().remove("my_tabs_active");
                activeProperty.set(curIndex.get());
                activeTab = ap;
                recWindows.setActiveNode(curIndex.get());
            }
        });
        activeProperty.addListener((_,  _, newValue) -> {
            if ( (int)newValue == curIndex.get()){
                ap.setShape(ACTIVE_SHARP);
                ap.getStyleClass().add("my_tabs_active");
            }else {
                ap.setShape(INACTIVE_SHARP);
                ap.getStyleClass().remove("my_tabs_active");
            }
        });
        tabs.getChildren().add(ap);
        return size;
    }


    public HBox getTabs() {
        return tabs;
    }

}
