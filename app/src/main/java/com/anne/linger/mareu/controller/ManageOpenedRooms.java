package com.anne.linger.mareu.controller;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.controller.activities.AddMeetingActivity;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;

import org.w3c.dom.EntityReference;

import java.time.LocalDate;
import java.util.List;

/**
*Created by Anne Linger on 19/01/2022.
*/
public class ManageOpenedRooms {

    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();

    //Select rooms which are opened according to date and time meeting
    public void checkOpenedRooms(LocalDate date, String time, List<Room> list, Context context) {
        for (Meeting meeting : mApiService.getMeetingList()) {
            if (meeting.getDate().equals(date) && meeting.getTime().equals(time)) {
                list.remove(meeting.getRoom());
            }
            else if (!list.contains(meeting.getRoom())){
                list.add(meeting.getRoom());
            }
        }
    }

    //Advertising if all rooms are reserved
    public void allRoomsReserved(List<Room> list, Context context) {
        if(list.isEmpty()) {
            Toast.makeText(context, R.string.allRoomsReserved, Toast.LENGTH_SHORT).show();
        }
    }

    //Enable the button Room when date and time are filled
    public static void enableButtonRoom(EditText editTextDate, EditText editTextTime, Button button, List<Room> list) {
        if (editTextDate.getText().toString().isEmpty() ||
                editTextTime.getText().toString().isEmpty() || list.isEmpty()) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }
}
