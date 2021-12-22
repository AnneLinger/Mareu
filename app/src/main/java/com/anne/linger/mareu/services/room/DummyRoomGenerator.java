package com.anne.linger.mareu.services.room;

import com.anne.linger.mareu.model.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*Created by Anne Linger on 21/12/2021.
*/
public abstract class DummyRoomGenerator {

    public static List<Room> DUMMY_ROOMS = Arrays.asList(
            new Room("Salle A", false),
            new Room("Salle B", false),
            new Room("Salle C", false),
            new Room("Salle D", false),
            new Room("Salle E", false),
            new Room("Salle F", false),
            new Room("Salle G", false),
            new Room("Salle H", false),
            new Room("Salle I", false),
            new Room("Salle J", false)
    );

    static List<Room> generateRooms() {
        return new ArrayList<>(DUMMY_ROOMS);
    }
}
