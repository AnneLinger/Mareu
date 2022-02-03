package com.anne.linger.mareu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Unit tests for the meetings
 */
public class MeetingUnitTest {
    private MeetingApiService mApiService;
    private final RoomApiService mRoomApiService = DIRoom.getNewInstanceRoomApiService();

    private final List<String> mCollaboratorList = Arrays.asList("test@lamzone.com", "test2@lamzone.com");
    private final Meeting mMeetingTest = new Meeting("Réunion test", mRoomApiService.getRoomList().get(0), Calendar.getInstance().getTime(), "14:00", "1 heure", mCollaboratorList, "Test");

    @Before
    public void setup() {
        mApiService = DIMeeting.getNewInstanceMeetingApiService();
    }

    //Get the meeting list
    @Test
    public void getTheMeetingList() {
        //Add a meeting to the meeting list
        mApiService.addMeeting(mMeetingTest);

        //Recover the meeting list with the service
        List<Meeting> meetingList = mApiService.getMeetingList();

        //Create a similar list to compare
        List<Meeting> expectedList = new ArrayList<>();
        expectedList.add(mMeetingTest);

        //Compare the two lists to prove that they are uniform
        assertThat(meetingList, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedList.toArray()));
    }

    //Add a meeting to the list
    @Test
    public void addAMeeting() {
        //First, the meeting list is empty
        List<Meeting> meetingList = mApiService.getMeetingList();
        assertTrue(meetingList.isEmpty());

        //Add a meeting with the service
        mApiService.addMeeting(mMeetingTest);

        //The meeting list is no longer empty
        assertFalse(meetingList.isEmpty());
    }

    //Delete a meeting from the list
    @Test
    public void deleteAMeeting() {
        //Add a meeting to the list
        mApiService.addMeeting(mMeetingTest);

        //The meeting list isn't empty
        assertFalse(mApiService.getMeetingList().isEmpty());

        //Delete the meeting from the list
        mApiService.deleteMeeting(mMeetingTest);

        //The meeting list is empty now
        assertTrue(mApiService.getMeetingList().isEmpty());
    }

    //Filter meetings by date
    @Test
    public void filterMeetingsByDate() {
        //Add a meeting with the current date
        mApiService.addMeeting(mMeetingTest);

        //Create a new meeting list and fill it with the mMeetingTest date filter
        List<Meeting> meetingListByDate = mApiService.getMeetingListByDate(mMeetingTest.getDate());

        //The meeting list isn't empty
        assertFalse(meetingListByDate.isEmpty());

        //Fill the meeting list with another date filter
        meetingListByDate = mApiService.getMeetingListByDate(Calendar.getInstance(Locale.CANADA).getTime());

        //The meeting list is empty now
        assertTrue(meetingListByDate.isEmpty());
    }

    //Filter meetings by room
    @Test
    public void filterMeetingsByRoom() {
        //Add a meeting with the position 0 room
        mApiService.addMeeting(mMeetingTest);

        //Create a new meeting list and fill it with the position 0 room filter
        List<Meeting> meetingListByRoom = mApiService.getMeetingListByRoom(mRoomApiService.getRoomList().get(0).getName());

        //The meeting list isn't empty
        assertFalse(meetingListByRoom.isEmpty());

        //Fill the meeting list with the position 1 room filter
        meetingListByRoom = mApiService.getMeetingListByRoom(mRoomApiService.getRoomList().get(1).getName());

        //The meeting list is empty now
        assertTrue(meetingListByRoom.isEmpty());
    }
}