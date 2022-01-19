package com.anne.linger.mareu.utils;

import android.content.Context;
import android.view.Menu;
import android.widget.Button;
import android.widget.PopupMenu;

import com.anne.linger.mareu.model.Room;

import java.util.List;

/**
*Created by Anne Linger on 18/01/2022.
*/
public class PopupUtils {

    public PopupMenu createPopupMenu(Context context, Button button) {
        PopupMenu popupMenu = new PopupMenu(context, button);
        return popupMenu;
    }

    public Menu createMenu(PopupMenu popupMenu){
        Menu menu = popupMenu.getMenu();
        return menu;
    }

    public void addStringItems(List<String> list, Menu menu){
        for (String string : list){
            menu.add(string);
        }
    }

    public void addRoomItems(List<Room> list, Menu menu) {
        for (Room room : list){
            menu.add(room.getName());
        }
    }
}
