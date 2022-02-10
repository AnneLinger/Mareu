package com.anne.linger.mareu.di;

import com.anne.linger.mareu.services.room.DummyRoomApiService;
import com.anne.linger.mareu.services.room.RoomApiService;

/**
 * Dependency injector to get instance of room service
 */
public class DIRoom {

    private static final RoomApiService service = new DummyRoomApiService();

    /**
     * Get an instance on @{@link RoomApiService}
     */
    public static RoomApiService getRoomApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link RoomApiService}, useful for tests, so we ensure the context is clean
     */
    public static RoomApiService getNewInstanceRoomApiService() {
        return new DummyRoomApiService();
    }
}
