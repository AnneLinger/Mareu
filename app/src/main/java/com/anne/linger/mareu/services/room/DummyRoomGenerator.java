package com.anne.linger.mareu.services.room;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.model.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*Created by Anne Linger on 21/12/2021.
*/
public abstract class DummyRoomGenerator {

    public static List<Room> DUMMY_ROOMS = Arrays.asList(
            new Room("Salle beige", R.drawable.beige_circle, false),
            new Room("Salle bleue", R.drawable.blue_circle,false),
            new Room("Salle grise", R.drawable.grey_circle, false),
            new Room("Salle jaune", R.drawable.yellow_circle, false),
            new Room("Salle orange", R.drawable.orange_circle, false),
            new Room("Salle rose", R.drawable.pink_circle, false),
            new Room("Salle rouge", R.drawable.red_circle, false),
            new Room("Salle turquoise", R.drawable.turquoise_circle, false),
            new Room("Salle verte", R.drawable.green_circle, false),
            new Room("Salle violette", R.drawable.purple_circle, false)
            );

    static List<Room> generateRooms() {
        return new ArrayList<>(DUMMY_ROOMS);
    }
}
