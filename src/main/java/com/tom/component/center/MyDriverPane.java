package com.tom.component.center;

import com.tom.component.console.MyLogListPane;
import com.tom.component.setting.MySetting;
import com.tom.component.top.AddressTab;
import com.tom.general.TabWatcher;
import com.tom.model.AddressProperty;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class MyDriverPane extends BorderPane implements TabWatcher<File> {

    private MainScrollPane mainScrollPane;

    private AddressTab addressTab;

    public MyDriverPane(AddressTab addressTab,MainScrollPane mainScrollPane) {
        this.mainScrollPane = mainScrollPane;
        this.addressTab = addressTab;
        this.setTop(addressTab.getAddressPane());
        this.setCenter(mainScrollPane);
        this.setBottom(MyLogListPane.getListView());
        BorderPane.setMargin(mainScrollPane,new Insets(0,5,0,20));
        BorderPane.setMargin(MyLogListPane.getListView(),new Insets(0,5,0,5));
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


    public static MyDriverPane createMyDriverPane(String curPath) {
        AddressProperty addressProperty = new AddressProperty(curPath, MySetting.getConfig().getBasePath());
        // 最内层的流布局
        MainFlowContentPart mainFlowContentPart = new MainFlowContentPart(addressProperty);

        // 中层的滚动布局
        MainScrollPane mainScrollPane = new MainScrollPane(mainFlowContentPart);

        // 地址栏组件
        AddressTab addressTab = new AddressTab(addressProperty,mainFlowContentPart);

        MyLogListPane myLogListPane = new MyLogListPane();

        // 最外层的方位布局组件
        return new MyDriverPane(addressTab,mainScrollPane);
    }
}
