package com.anne.linger.mareu.services.meeting;

import com.anne.linger.mareu.model.Meeting;

import java.util.List;

/**
 * Created by Anne Linger on 20/12/2021.
 */
public interface MeetingApiService {

    /**
     * Recover the meeting list
     */
    List<Meeting> getMeetingList();

    /**
     * Add a meeting
     * @param meeting
     */
    void addMeeting(Meeting meeting);

    /**
     * Remove a meeting
     * @param meeting
     */
    void removeMeeting(Meeting meeting);

    /**
     * Recover the collaborator List
     */
    List<String> getCollaboratorList();

    /**
     * Recover the dummy collaborator List
     */
    List<String> getDummyCollaboratorList();

    /**
     * Add a collaborator
     * @param collaborator
     */
    void addCollaborator(String collaborator);

    /**
     * Remove a collaborator
     * @param collaborator
     */
    void removeCollaborator(String collaborator);

    /**
     * Clear the collaborator list
     */
    void clearCollaboratorList();
}
