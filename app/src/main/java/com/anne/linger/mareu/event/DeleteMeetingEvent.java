package com.anne.linger.mareu.event;

import com.anne.linger.mareu.model.Meeting;

/**
 * Event called when a user wants to delete a meeting
 */
public class DeleteMeetingEvent {
    /**
     * Meeting to delete
     */
    public final Meeting meeting;

    /**
     * Constructor
     *
     * @param meeting
     */
    public DeleteMeetingEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}
