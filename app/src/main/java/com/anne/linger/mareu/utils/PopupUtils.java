package com.anne.linger.mareu.utils;

import android.content.Context;
import android.view.Menu;
import android.widget.Button;
import android.widget.PopupMenu;

import com.anne.linger.mareu.model.Room;

import java.util.List;

/**
* Useful methods for popup menus
*/
public class PopupUtils {

    //Create a Popup menu
    public PopupMenu createPopupMenu(Context context, Button button) {
        PopupMenu popupMenu = new PopupMenu(context, button);
        return popupMenu;
    }

    //Create a menu in a Popup menu
    public Menu createMenu(PopupMenu popupMenu){
        Menu menu = popupMenu.getMenu();
        return menu;
    }
}
