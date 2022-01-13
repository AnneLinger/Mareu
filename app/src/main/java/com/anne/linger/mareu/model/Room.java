package com.anne.linger.mareu.model;
/**
*Created by Anne Linger on 21/12/2021.
*/
public class Room {
    private String name;
    private int drawable;
    private Boolean isReserved;

    public Room(String name, int drawable, Boolean isReserved) {
        this.name = name;
        this.drawable = drawable;
        this.isReserved = isReserved;
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

    public Boolean getReserved() {
        return isReserved;
    }

    public void setReserved(Boolean reserved) {
        isReserved = reserved;
    }
}
