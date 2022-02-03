package com.anne.linger.mareu.services.room;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.model.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generates a list of dummy rooms
 */
public abstract class DummyRoomGenerator {

    public static final List<Room> DUMMY_ROOMS = Arrays.asList(
            new Room("Salle bleue", R.drawable.blue_circle),
            new Room("Salle grise", R.drawable.grey_circle),
            new Room("Salle jaune", R.drawable.yellow_circle),
            new Room("Salle marron", R.drawable.brown_circle),
            new Room("Salle orange", R.drawable.orange_circle),
            new Room("Salle rose", R.drawable.pink_circle),
            new Room("Salle rouge", R.drawable.red_circle),
            new Room("Salle turquoise", R.drawable.turquoise_circle),
            new Room("Salle verte", R.drawable.green_circle),
            new Room("Salle violette", R.drawable.purple_circle)
    );

    static List<Room> generateRooms() {
        return new ArrayList<>(DUMMY_ROOMS);
    }
}
