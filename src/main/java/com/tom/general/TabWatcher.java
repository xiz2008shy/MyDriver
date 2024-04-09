package com.tom.general;

import javafx.beans.property.Property;

public interface TabWatcher<W> {


    Property<W> getWatcher();

    String getInitTitle();

    String refreshTitle(W oldValue, W newValue);

}
