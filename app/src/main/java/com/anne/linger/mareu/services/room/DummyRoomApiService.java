package com.anne.linger.mareu.services.room;

import com.anne.linger.mareu.model.Room;

import java.util.ArrayList;
import java.util.List;

/**
*Implements the room interface
*/
public class DummyRoomApiService implements RoomApiService {

    public List<Room> roomList = DummyRoomGenerator.generateRooms();

    @Override
    public List<Room> getRoomList() {
        return roomList;
    }
}
