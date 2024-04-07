package com.tom.pane;

import javafx.beans.property.Property;

public interface TabWatcher<W> {


    Property<W> getWatcher();

    String getInitTitle();

    String refreshTitle(W oldValue, W newValue);

}
