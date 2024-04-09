package com.tom;

import com.tom.component.center.MainFlowContentPart;
import com.tom.component.center.MainScrollPane;
import com.tom.component.center.MyDriverPane;
import com.tom.component.top.AddressTab;
import com.tom.menu.BaseMenu;
import com.tom.menu.MyMenuContext;
import com.tom.model.AddressProperty;
import com.tom.general.RecWindows;
import com.tom.utils.ImageUtils;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FileManagementApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private final String curPath = "C:\\Users\\TOMQI\\Desktop";

    @Override
    public void start(Stage stage) {

        MyDriverPane myDriverPane = createMyDriverPane();

        RecWindows recWindowsPane = new RecWindows(myDriverPane, 900.0,
                600.0, 12.0, stage);
        recWindowsPane.initStage();

        BaseMenu baseMenu = new BaseMenu(200, 30,recWindowsPane);

        MyMenuContext menu1 = new MyMenuContext(new Label("复制"), baseMenu);
        menu1.whenActiveByMouse( e -> {
            System.out.println("menu1 active!");
        });
        MyMenuContext menu2 = new MyMenuContext(new Label("粘贴"), baseMenu);
        menu2.whenActiveByMouse( e -> {
            System.out.println("menu2 active!");
        });

        myDriverPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.out.println("menu mouse clicked!");
            e.consume();
            if (e.getButton().equals(MouseButton.SECONDARY)){
                baseMenu.showMenu(e,recWindowsPane);
            }else if(baseMenu.isShow()){
                baseMenu.closeMenu();
            }
        });

       /* MyDriverPane myDriverPane2 = createMyDriverPane();
        recWindowsPane.createNewTab(myDriverPane2,true);*/

        // 增加图标
        stage.getIcons().add(ImageUtils.getImageFromResources("/img/fileDir32.png",32,32));
        stage.show();
    }

    private MyDriverPane createMyDriverPane() {
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
