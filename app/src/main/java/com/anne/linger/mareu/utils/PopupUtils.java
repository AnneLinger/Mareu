package com.anne.linger.mareu.utils;

import android.content.Context;
import android.view.Menu;
import android.widget.Button;
import android.widget.PopupMenu;

/**
 * Useful methods for popup menus
 */
public class PopupUtils {

    //Create a Popup menu
    public PopupMenu createPopupMenu(Context context, Button button) {
        return new PopupMenu(context, button);
    }

    //Create a menu in a Popup menu
    public Menu createMenu(PopupMenu popupMenu) {
        return popupMenu.getMenu();
    }
}
