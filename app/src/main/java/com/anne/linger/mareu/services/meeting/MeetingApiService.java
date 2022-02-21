package com.anne.linger.mareu.services.meeting;

import com.anne.linger.mareu.model.Meeting;

import java.util.Date;
import java.util.List;

/**
 * Interface to manage the meeting lists
 */
public interface MeetingApiService {

    /**
     * Recover the meeting list
     */
    List<Meeting> getMeetingList();

    /**
     * Add a meeting
     *
     * @param meeting
     */
    void addMeeting(Meeting meeting);

    /**
     * Remove a meeting
     *
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Filter meetings by date
     *
     * @param date
     */
    List<Meeting> getMeetingListByDate(Date date);

    /**
     * Filter meetings by room
     *
     * @param roomName
     */
    List<Meeting> getMeetingListByRoom(String roomName);

    /**
     * Recover the dummy collaborator List
     */
    List<String> getDummyCollaboratorList();

    /**
     * Recover the dummy time List
     */
    List<String> getDummyTimeList();
}
