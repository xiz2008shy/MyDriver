package com.tom.general.menu;

import javafx.event.Event;

public interface ShowMenu<T extends Event> {

    void showMenu(T event,BaseMenu baseMenu);
    void closeMenu(T event,BaseMenu baseMenu);

    void setMenuWidth(double menuWidth);
    void setMenuHeight(double menuHeight);

}
