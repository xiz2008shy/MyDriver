package com.tom.component.center;

import com.tom.component.top.AddressTab;
import com.tom.general.RecWindows;
import com.tom.general.TabWatcher;
import com.tom.menu.BaseMenu;
import com.tom.menu.MyMenuContext;
import com.tom.model.AddressProperty;
import com.tom.utils.DeliverUtils;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.List;

public class MyDriverPane extends BorderPane implements TabWatcher<File> {

    private MainScrollPane mainScrollPane;

    private AddressTab addressTab;

    private BaseMenu baseMenu;

    public MyDriverPane(AddressTab addressTab,MainScrollPane mainScrollPane) {
        this.mainScrollPane = mainScrollPane;
        this.addressTab = addressTab;
        this.setTop(addressTab.getAddressPane());
        this.setCenter(mainScrollPane);
        BorderPane.setMargin(mainScrollPane,new Insets(0,5,0,20));
    }

    @Override
    public Property<File> getWatcher() {
        return mainScrollPane.getWatcher();
    }

    @Override
    public String getInitTitle() {
        return mainScrollPane.getInitTitle();
    }

    public MainScrollPane getMainScrollPane() {
        return mainScrollPane;
    }

    @Override
    public String refreshTitle(File oldValue, File newValue) {
        return mainScrollPane.refreshTitle(oldValue,newValue);
    }

    public static void addMenu(RecWindows recWindowsPane, Node node){
        MyDriverPane pane = (MyDriverPane) node;
        if (pane.baseMenu == null) {
            BaseMenu bm = DeliverUtils.getBaseMenu();
            if ( bm != null){
                pane.baseMenu = bm;
            }else {
                pane.baseMenu = pane.createBaseMenu(recWindowsPane);
            }
        }
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.out.println("menu mouse clicked!");
            e.consume();
            if(pane.baseMenu.isShow()){
                pane.baseMenu.closeMenu();
            }
            String id = e.getPickResult().getIntersectedNode().getId();
            File file = null;
            if (id != null) {
                int index = id.indexOf("_");
                if (index > -1){
                    id = id.substring(0,index);
                }
                file = DeliverUtils.getPathIndex().get(id);
                if (file != null ){
                    DeliverUtils.setCurPath(file);
                }
            }
            if (file == null) {
                AnchorPane selectedFileNode = DeliverUtils.getLastSelectedFileNode();
                if (selectedFileNode != null) {
                    ObservableList<String> styleClass = selectedFileNode.getStyleClass();
                    styleClass.add("my_icon");
                    styleClass.remove("my_icon_click");
                }
            }
            if (e.getButton().equals(MouseButton.SECONDARY)){
                pane.baseMenu.showMenu(e,recWindowsPane);
            }
        });
    }

    private BaseMenu createBaseMenu(RecWindows recWindowsPane) {
        BaseMenu baseMenu = new BaseMenu(160, 40, recWindowsPane);

        MyMenuContext open = new MyMenuContext(new Label("打开"), baseMenu);
        open.whenActiveByMouse( e -> {
            System.out.println("open active!");
        });
        open.setDisabledPredicate(_ -> {
            File curPath = DeliverUtils.getCurPath();
            return curPath == null;
        });
        MyMenuContext menu0 = new MyMenuContext(new Label("新标签页中打开"), baseMenu);
        menu0.whenActiveByMouse( e -> {
            MyDriverPane myDriverPane2 = this.createMyDriverPane(DeliverUtils.getCurPath().getAbsolutePath());
            recWindowsPane.createNewTab(myDriverPane2,true);
        });
        menu0.setDisabledPredicate(_ -> {
            File curPath = DeliverUtils.getCurPath();
            return curPath == null || !curPath.isDirectory();
        });
        MyMenuContext menu1 = new MyMenuContext(new Label("复制"), baseMenu);
        menu1.whenActiveByMouse( e -> {
            System.out.println("menu1 active!");
        });
        menu1.setDisabledPredicate(_ -> {
            File curPath = DeliverUtils.getCurPath();
            return curPath == null;
        });
        MyMenuContext menu2 = new MyMenuContext(new Label("粘贴"), baseMenu);
        menu2.whenActiveByMouse( e -> {
            System.out.println("menu2 active!");
        });

        menu2.setDisabledPredicate(_ -> {
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            List<File> files = systemClipboard.getFiles();
            return files == null || files.isEmpty();
        });

        baseMenu.setCloseMenuHandler(m -> {
            DeliverUtils.clearCurPath();
        });
        return baseMenu;
    }



    public static MyDriverPane createMyDriverPane(String curPath) {
        AddressProperty addressProperty = new AddressProperty(curPath);
        // 最内层的流布局
        MainFlowContentPart mainFlowContentPart = new MainFlowContentPart(addressProperty);

        // 中层的滚动布局
        MainScrollPane mainScrollPane = new MainScrollPane(mainFlowContentPart);

        // 地址栏组件
        AddressTab addressTab = new AddressTab(addressProperty,mainFlowContentPart);

        // 最外层的方位布局组件
        return new MyDriverPane(addressTab,mainScrollPane);
    }
}
