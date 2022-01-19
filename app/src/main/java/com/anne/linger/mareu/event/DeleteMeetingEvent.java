package com.anne.linger.mareu.event;

import com.anne.linger.mareu.model.Meeting;

/**
*Created by Anne Linger on 17/01/2022.
*/public class DeleteMeetingEvent {
    /**
     * Meeting to delete
     */
    public Meeting meeting;

    /**
     * Constructor.
     * @param meeting
     */
    public DeleteMeetingEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}
