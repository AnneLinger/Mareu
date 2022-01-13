package com.anne.linger.mareu.services.meeting;

import com.anne.linger.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.List;

/**
*Created by Anne Linger on 20/12/2021.
*/
public class NewMeetingApiService implements MeetingApiService {

    public List<Meeting> meetingList = new ArrayList();
    public List<String> collaboratorList = new ArrayList();
    public List<String> dummyCollaboratorList =  DummyCollaboratorGenerator.generateCollaborators();

    @Override
    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    @Override
    public void addMeeting(Meeting meeting) {
        meetingList.add(meeting);
    }

    @Override
    public void removeMeeting(Meeting meeting) {
        meetingList.remove(meeting);
    }

    @Override
    public List<String> getCollaboratorList() {
        return collaboratorList;
    }

    @Override
    public List<String> getDummyCollaboratorList() {
        return dummyCollaboratorList;
    }

    @Override
    public void addCollaborator(String collaborator) {
        collaboratorList.add(collaborator);
    }

    @Override
    public void removeCollaborator(String collaborator) {
        collaboratorList.remove(collaborator);
    }

    @Override
    public void clearCollaboratorList() {
        collaboratorList.removeAll(collaboratorList);
    }
}
