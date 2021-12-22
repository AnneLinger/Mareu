package com.anne.linger.mareu.services.room;

import com.anne.linger.mareu.model.Room;

import java.util.List;

/**
 * Created by Anne Linger on 21/12/2021.
 */
public interface RoomApiService {

    /**
     * Recover the room List
     */
    List<Room> getRoomList();

    /**
     * Check if a room is reserved
     * @param room
     */
    boolean isReserved(Room room);
}
