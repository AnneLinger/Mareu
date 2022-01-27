package com.anne.linger.mareu.controller.manager;

import static java.time.LocalDate.parse;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.controller.activities.AddMeetingActivity;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;
import com.anne.linger.mareu.utils.ChipUtils;
import com.anne.linger.mareu.utils.PopupUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.EntityReference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
*Class to manage creation of a new meeting (for AddMeetingActivity)
*/
public class ManageAddMeeting {

    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final PopupUtils popupUtils = new PopupUtils();
    private static final ChipUtils chipUtils = new ChipUtils();

    //Recover the date from the DatePickerDialog
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker, int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    //Listener on the date EditText to check the date format
    public void checkTheDateFormat(EditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editText.getText().toString().matches("([0-9]{2}/([0-9]{2})/([0-9]{4}))")) {
                    editText.setError("Veuillez entrer un format JJ/MM/AAAA");
                }
                else{
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    //Convert String date to LocalDate
    //TODO COMMENT L'UTILISER ??
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate convertDate(String dateString, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatter = formatter.withLocale(Locale.FRANCE);
        date = parse(dateString, formatter);
        return date;
    }

    //Recover the time from the TimePickerDialog
    public static java.util.Date getTimeFromTimePicker(TimePicker timePicker, int hourOfDay, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        return calendar.getTime();
    }

    //Listener on the time EditText to check the time format
    public void checkTheTimeFormat(EditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editText.getText().toString().matches("[0-9]{2}[:][0-9]{2}")) {
                    editText.setError("Veuillez entrer un format HH:MM");
                }
                else{
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    //Listener on time or date EditText to enable the room button
    public void enableTheRoomSelection(EditText editText, EditText editTextDate, EditText editTextTime, Button button, List<Room> list) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextDate.getText().toString().isEmpty() ||
                        editTextTime.getText().toString().isEmpty() || list.isEmpty()) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
        });
    }


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
        Log.e("tag", editTextTime.getText().toString());
        if (editTextDate.getText().toString().isEmpty() ||
                editTextTime.getText().toString().isEmpty() || list.isEmpty()) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }

    //Listener to select a room
    public void selectARoom(Button button, List<Room> list) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = popupUtils.createPopupMenu(button.getContext(), button);
                Menu menu = popupUtils.createMenu(popupMenu);
                for (Room room : list){
                    menu.add(room.getName());
                }
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        button.setText(menuItem.getTitle());
                        return true;
                    }
                });
            }
        });
    }

    //Save the selected room
    // TODO : COMMENT L'UTILISER ??
    public Room saveTheRoom(List<Room> list, Button button, Room room) {
        for (Room mRoom : list){
            if(button.getText().toString().equals(mRoom.getName())){
                room = mRoom;
            }
        }
        return room;
    }

    //Listener to select a duration
    public void chooseADuration(Button button, List<String> list) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = popupUtils.createPopupMenu(button.getContext(), button);
                Menu menu = popupUtils.createMenu(popupMenu);
                for (String string : list){
                    menu.add(string);
                }
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        button.setText(menuItem.getTitle());
                        return true;
                    }
                });
            }
        });
    }

    //Listener to type a collaborator
    public void typeCollaborator(EditText editText, Button button){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editText.getText().toString();
                if (!string.matches("^(.+)@lamzone.com")) {
                    editText.setError("Veuillez entrer un format nom@lamzone.com");
                }
                else{
                    button.setEnabled(true);
                }
            }
        });
    }

    //Listener to save a collaborator
    public void saveCollaborator(Button button, EditText editText, List<String> list, ChipGroup chipGroup) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = editText.getText().toString();
                list.add(string);
                editText.clearFocus();
                editText.setText(null);
                editText.setError(null, null);
                LayoutInflater inflater = LayoutInflater.from(chipGroup.getContext());
                Chip chip = (Chip) inflater.inflate(R.layout.chip_entry, chipGroup, false);
                chip.setText(string);
                chip.setCloseIconVisible(true);
                chipGroup.addView(chip);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chipUtils.deleteAChipFromAView((Chip) view);
                        chipUtils.deleteAChipFromAList((Chip) view, list);
                    }
                });
            }
        });
    }
}
