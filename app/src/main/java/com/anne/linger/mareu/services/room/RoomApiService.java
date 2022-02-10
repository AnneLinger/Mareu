package com.anne.linger.mareu.services.room;

import com.anne.linger.mareu.model.Room;

import java.util.List;

/**
 * Interface to manage the room list
 */
public interface RoomApiService {

    /**
     * Recover the room List
     */
    List<Room> getRoomList();
}
