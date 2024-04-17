package com.tom.component.menu;


import com.tom.controller.MyDriverPaneController;
import com.tom.general.RecWindows;
import com.tom.general.menu.BaseMenu;
import com.tom.general.menu.MyMenuContext;
import com.tom.handler.fxml.DesktopIconClickHandler;
import com.tom.model.ModelData;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class RightClickMenu {


    /**
     * <StackPane> - secPane (recWindows)
     *     <HBox> - realPane(this.realPane)
     *         <BaseMenu> - this
     *             <ImageView></ImageView> - background
     *             <VBox> - menuContent(this)
     *                 <HBox> - MyMenuContent
     *                      <Label></Label>
     *                 </HBox>
     *             </VBox>
     *         </BaseMenu>
     *     </HBox>
     * </StackPane>
     * @param windows 窗口对象
     * @return 返回右键菜单对象
     */
    public static BaseMenu createBaseMenu(RecWindows windows) {
        BaseMenu baseMenu = new BaseMenu(160, 40, windows);

        MyMenuContext open = new MyMenuContext(new Label("打开"), baseMenu);
        open.whenActiveByMouse( _ -> {
            File file = windows.getActiveModelData().getRealSelectedFile();
            if (file.isDirectory()){
                windows.getActiveModelData().setFile(file);
            }else {
                DesktopIconClickHandler.executeFile(file);
            }
        });
        open.setDisabledPredicate(_ -> {
            File curPath = windows.getActiveModelData().getRealSelectedFile();
            return curPath == null;
        });
        MyMenuContext menu0 = new MyMenuContext(new Label("新标签页中打开"), baseMenu);
        menu0.whenActiveByMouse( _ -> {
            File file = windows.getActiveModelData().getRealSelectedFile();
            MyDriverPaneController myDriverPane2 = new MyDriverPaneController(file);
            windows.createNewTab(myDriverPane2,myDriverPane2.getModelData(),true);
        });
        menu0.setDisabledPredicate(_ -> {
            File curPath = windows.getActiveModelData().getRealSelectedFile();
            log.info("menu0.setDisabledPredicate curPath={}",curPath);
            return curPath == null || !curPath.isDirectory();
        });
        MyMenuContext menu1 = new MyMenuContext(new Label("复制"), baseMenu);
        menu1.whenActiveByMouse( e -> {
            log.info("复制 active!");
        });
        menu1.setDisabledPredicate(_ -> {
            File curPath = windows.getActiveModelData().getRealSelectedFile();
            return curPath == null;
        });
        MyMenuContext menu2 = new MyMenuContext(new Label("粘贴"), baseMenu);
        menu2.whenActiveByMouse( e -> {
            log.info("粘贴 active!");
        });

        menu2.setDisabledPredicate(_ -> {
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            List<File> files = systemClipboard.getFiles();
            return files == null || files.isEmpty();
        });

        /*baseMenu.setCloseMenuHandler(m -> {
            windows.getActiveModelData().setSelectedFile(null);
        });*/
        return baseMenu;
    }



    public static void addMenu(RecWindows windows){
        BaseMenu bm = windows.getBaseMenu();
        if (windows.getBaseMenu() == null) {
            bm = RightClickMenu.createBaseMenu(windows);
            windows.setBaseMenu(bm);
        }
        bm.closeMenu();
        ObservableMap<Object, Object> myMap = windows.getProperties();
        if (myMap.get("isAddMenuHandler") == null){
            BaseMenu finalBm = bm;
            windows.getSecPane().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                log.info("menu mouse clicked!");
                e.consume();
                if(finalBm.isShow()){
                    finalBm.closeMenu();
                }
                Node intersectedNode = e.getPickResult().getIntersectedNode();
                String id = intersectedNode.getId();
                File file = null;
                ModelData modelData = windows.getActiveModelData();
                if (id != null || (intersectedNode.getParent() != null && (id = intersectedNode.getParent().getId()) != null)) {
                    int index = id.indexOf("_");
                    if (index > -1){
                        id = id.substring(0,index);
                    }
                    file = modelData.getCacheMap().get(id);
                }
                log.info("menu target node={}",e.getPickResult().getIntersectedNode());
                log.info("menu target id={}",id);
                log.info("menu target id={}",file);
                if (file == null) {
                    AnchorPane selectedFileNode = modelData.getSelectedFile();
                    if (selectedFileNode != null) {
                        ObservableList<String> styleClass = selectedFileNode.getStyleClass();
                        styleClass.remove("my_icon_click");
                        modelData.setRealSelectedFile(null);
                        modelData.setSelectedFile(null);
                    }
                }
                if (e.getButton().equals(MouseButton.SECONDARY)){
                    finalBm.showMenu(e,windows);
                }
            });
            myMap.put("isAddMenuHandler",true);
        }
    }
}
