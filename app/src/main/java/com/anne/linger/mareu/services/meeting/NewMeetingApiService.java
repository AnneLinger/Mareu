package com.anne.linger.mareu.services.meeting;

import com.anne.linger.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.List;

/**
*Created by Anne Linger on 20/12/2021.
*/
public class NewMeetingApiService implements MeetingApiService {

    public List<Meeting> meetingList = new ArrayList();
    public List<String> dummyCollaboratorList =  DummyCollaboratorGenerator.generateCollaborators();
    public List<String> dummyDurationList = DummyDurationGenerator.generateDurations();

    @Override
    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    @Override
    public void addMeeting(Meeting meeting) {
        meetingList.add(meeting);
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetingList.remove(meeting);
    }

    @Override
    public List<String> getDummyCollaboratorList() {
        return dummyCollaboratorList;
    }

    @Override
    public List<String> getDummyDurationList() {
        return dummyDurationList;
    }
}
