package com.tom.component.menu;

import com.tom.component.center.MainFlowContentPart;
import com.tom.component.center.MyDriverPane;
import com.tom.general.RecWindows;
import com.tom.general.menu.BaseMenu;
import com.tom.general.menu.MyMenuContext;
import com.tom.handler.icon.DesktopIconClickHandler;
import com.tom.utils.DeliverUtils;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.List;

public class RightClickMenu {

    public static final String curPath = "C:\\Users\\TOMQI\\Desktop";


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
     * @param recWindowsPane
     * @return
     */
    public static BaseMenu createBaseMenu(RecWindows recWindowsPane) {
        BaseMenu baseMenu = new BaseMenu(160, 40, recWindowsPane);

        MyMenuContext open = new MyMenuContext(new Label("打开"), baseMenu);
        open.whenActiveByMouse( _ -> {
            File file = DeliverUtils.getCurPath();
            if (file.isDirectory()){
                MyDriverPane myDriverPane = (MyDriverPane)recWindowsPane.getActiveNode();
                MainFlowContentPart mainFlowPart = myDriverPane.getMainScrollPane().getMainFlowContentPart();
                mainFlowPart.getAddressProperty().setCurPath(file);
                mainFlowPart.refreshFileNode();
            }else {
                DesktopIconClickHandler.executeFile(file);
            }
        });
        open.setDisabledPredicate(_ -> {
            File curPath = DeliverUtils.getCurPath();
            return curPath == null;
        });
        MyMenuContext menu0 = new MyMenuContext(new Label("新标签页中打开"), baseMenu);
        menu0.whenActiveByMouse( _ -> {
            MyDriverPane myDriverPane2 = MyDriverPane.createMyDriverPane(DeliverUtils.getCurPath().getAbsolutePath(), curPath);
            recWindowsPane.createNewTab(myDriverPane2,true);
        });
        menu0.setDisabledPredicate(_ -> {
            File curPath = DeliverUtils.getCurPath();
            System.out.println(curPath);
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



    public static void addMenu(RecWindows windows){
        BaseMenu bm = DeliverUtils.getBaseMenu();
        if (windows.getBaseMenu() == null) {
            bm = RightClickMenu.createBaseMenu(windows);
            windows.setBaseMenu(bm);
            DeliverUtils.setBaseMenu(bm);
        }
        bm.closeMenu();
        ObservableMap<Object, Object> myMap = windows.getProperties();
        if (myMap.get("isAddMenuHandler") == null){
            BaseMenu finalBm = bm;
            windows.getSecPane().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                System.out.println("menu mouse clicked!");
                e.consume();
                if(finalBm.isShow()){
                    finalBm.closeMenu();
                }
                Node intersectedNode = e.getPickResult().getIntersectedNode();
                String id = intersectedNode.getId();
                File file = null;
                if (id != null || (intersectedNode.getParent() != null && (id = intersectedNode.getParent().getId()) != null)) {
                    int index = id.indexOf("_");
                    if (index > -1){
                        id = id.substring(0,index);
                    }
                    file = DeliverUtils.getPathIndex(windows.getTopBar().getActiveIndex()).get(id);
                    if (file != null ){
                        DeliverUtils.setCurPath(file);
                    }
                }
                System.out.println(e.getPickResult().getIntersectedNode());
                System.out.println(id);
                System.out.println(file);
                if (file == null) {
                    AnchorPane selectedFileNode = DeliverUtils.getLastSelectedFileNode();
                    if (selectedFileNode != null) {
                        ObservableList<String> styleClass = selectedFileNode.getStyleClass();
                        styleClass.remove("my_icon_click");
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
