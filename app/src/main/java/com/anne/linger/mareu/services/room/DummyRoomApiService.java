package com.anne.linger.mareu.services.room;

import com.anne.linger.mareu.model.Room;

import java.util.ArrayList;
import java.util.List;

/**
*Created by Anne Linger on 21/12/2021.
*/
public class DummyRoomApiService implements RoomApiService {

    public List<Room> roomList = DummyRoomGenerator.generateRooms();

    @Override
    public List<Room> getRoomList() {
        return roomList;
    }

    @Override
    public boolean isReserved(Room room) {
        return room.getReserved();
    }
}
