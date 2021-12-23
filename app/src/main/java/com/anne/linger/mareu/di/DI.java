package com.anne.linger.mareu.di;

import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.meeting.NewMeetingApiService;

/**
*Created by Anne Linger on 23/12/2021
 * Dependency injector to get instance of service
*/
public class DI {

    private static MeetingApiService service = new NewMeetingApiService();

    /**
     * Get an instance on @{@link MeetingApiService}
     * @return
     */
    public static MeetingApiService getMeetingApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link MeetingApiService}, useful for tests, so we ensure the context is clean
     * @return
     */
    public static MeetingApiService getNewInstanceMeetingApiService() {
        return new NewMeetingApiService();
    }
}
