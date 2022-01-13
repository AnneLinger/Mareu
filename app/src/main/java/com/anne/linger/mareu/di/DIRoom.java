package com.anne.linger.mareu.di;

import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.meeting.NewMeetingApiService;
import com.anne.linger.mareu.services.room.DummyRoomApiService;
import com.anne.linger.mareu.services.room.RoomApiService;

/**
*Created by Anne Linger on 03/01/2022
 *  * Dependency injector to get instance of service
*/public class DIRoom {

    private static RoomApiService service = new DummyRoomApiService();

    /**
     * Get an instance on @{@link RoomApiService}
     * @return
     */
    public static RoomApiService getRoomApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link RoomApiService}, useful for tests, so we ensure the context is clean
     * @return
     */
    public static RoomApiService getNewInstanceRoomApiService() {
        return new DummyRoomApiService();
    }
}
