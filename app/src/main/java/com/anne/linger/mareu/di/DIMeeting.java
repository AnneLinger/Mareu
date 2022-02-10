package com.anne.linger.mareu.di;

import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.meeting.NewMeetingApiService;

/**
 * Dependency injector to get instance of meeting service
 */
public class DIMeeting {

    private static final MeetingApiService service = new NewMeetingApiService();

    /**
     * Get an instance on @{@link MeetingApiService}
     */
    public static MeetingApiService getMeetingApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link MeetingApiService}, useful for tests, so we ensure the context is clean
     */
    public static MeetingApiService getNewInstanceMeetingApiService() {
        return new NewMeetingApiService();
    }
}
