package com.anne.linger.mareu.services.meeting;

import com.anne.linger.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements the meeting interface
 */
public class NewMeetingApiService implements MeetingApiService {

    public final List<Meeting> meetingList = new ArrayList();
    public final List<String> dummyCollaboratorList = DummyCollaboratorGenerator.generateCollaborators();
    public final List<String> dummyDurationList = DummyDurationGenerator.generateDurations();

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
    public List<Meeting> getMeetingListByDate(Date date) {
        List<Meeting> meetingsByDate = new ArrayList<>();
        for (Meeting meeting : meetingList)
            if (meeting.getDate().equals(date)) {
                meetingsByDate.add(meeting);
            }
        return meetingsByDate;
    }

    @Override
    public List<Meeting> getMeetingListByRoom(String roomName) {
        List<Meeting> meetingsByRoom = new ArrayList<>();
        for (Meeting meeting : meetingList)
            if (meeting.getRoom().getName().equals(roomName)) {
                meetingsByRoom.add(meeting);
            }
        return meetingsByRoom;
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
