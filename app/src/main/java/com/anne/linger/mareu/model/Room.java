package com.anne.linger.mareu.model;
/**
*Model class for a room
*/
public class Room {
    private String name;
    private int drawable;

    public Room(String name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

}
