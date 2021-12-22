package com.anne.linger.mareu.model;
/**
*Created by Anne Linger on 21/12/2021.
*/
public class Room {
    private String name;
    private Boolean isReserved;

    public Room(String name, Boolean isReserved) {
        this.name = name;
        this.isReserved = isReserved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getReserved() {
        return isReserved;
    }

    public void setReserved(Boolean reserved) {
        isReserved = reserved;
    }
}
