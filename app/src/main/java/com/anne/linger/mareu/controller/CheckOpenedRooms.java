package com.anne.linger.mareu.controller;

import android.content.Context;
import android.widget.Toast;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;

import java.time.LocalDate;
import java.util.List;

/**
*Created by Anne Linger on 19/01/2022.
*/
public class CheckOpenedRooms {

    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final RoomApiService mRoomApiService = DIRoom.getRoomApiService();

    //Check which rooms are opened according to date meeting
    public void checkOpenedRooms(LocalDate date, List<Room> list, Context context) {
        for (Meeting meeting : mApiService.getMeetingList()){
            if (meeting.getDate().equals(date)){
                list.remove(meeting.getRoom());
            }
        }
        if (list.isEmpty()) {
            Toast.makeText(context, R.string.allRoomsReserved, Toast.LENGTH_SHORT).show();
        }
    }
}
