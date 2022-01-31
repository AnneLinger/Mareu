package com.anne.linger.mareu.controller.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.controller.manager.ManageAddMeeting;
import com.anne.linger.mareu.databinding.ActivityAddMeetingBinding;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
*Activity to add a new meeting to the recycler view
*/
public class AddMeetingActivity extends AppCompatActivity {

    private static ActivityAddMeetingBinding mBinding;
    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final RoomApiService mRoomApiService = DIRoom.getRoomApiService();
    private static final ManageAddMeeting manageAddMeeting = new ManageAddMeeting();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private boolean is24HView = true;
    private int lastSelectedHour;
    private int lastSelectedMinute;
    public Date date;
    private String time;
    private List<String> durationList = mApiService.getDummyDurationList();
    private List<Room> openedRooms = mRoomApiService.getRoomList();
    private Room room;
    private List<String> collaboratorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        selectDate();
        saveTheDate();
        selectTime();
        saveTheTime();
        selectARoom();
        selectADuration();
        configureAutoCompleteCollaborator();
        addCollaborator();
        addTopic();
        saveMeeting();
    }

    //Clear focus when user touches anywhere
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    //Configure the UI
    @SuppressLint("ResourceAsColor")
    private void initUi() {
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        mBinding.buttonRooms.setEnabled(false);
        mBinding.buttonSave.setEnabled(false);
        setContentView(view);
        configureToolbar();
    }

    //Toolbar configuration
    private void configureToolbar() {
        //Toolbar as ActionBar
        setSupportActionBar(mBinding.tbAdd);
        //Back button enable
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Select the date in the DatePickerDialog
    private void selectDate(){
        mBinding.tfDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDatePickerDialog();
            }
        });

        //Get current date
        final Calendar calendar = Calendar.getInstance();
        this.lastSelectedYear = calendar.get(Calendar.YEAR);
        this.lastSelectedMonth = calendar.get(Calendar.MONTH);
        this.lastSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //Configure the DatePickerDialog
    private void configureDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date = manageAddMeeting.getDateFromDatePicker(datePicker, year, monthOfYear, dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String result = formatter.format(date);
                try {
                    date = formatter.parse(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mBinding.etDate.setText(result);
                mBinding.tfDate.setErrorEnabled(false);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDay = dayOfMonth;
            }
        };
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(this, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);
        datePickerDialog.show();
    }

    //Display the date in the EditText to save it
    private void saveTheDate() {
        manageAddMeeting.checkTheDateFormat(mBinding.etDate, mBinding.tfDate);
        manageAddMeeting.checkOpenedRooms(date, time, openedRooms, AddMeetingActivity.this);
        manageAddMeeting.allRoomsReserved(openedRooms, AddMeetingActivity.this);
        manageAddMeeting.enableTheRoomSelection(mBinding.etDate, mBinding.etDate, mBinding.etTime, mBinding.buttonRooms, openedRooms);
        enableButtonSave();
    }

    //Select the time in the TimePickerDialog
    private void selectTime() {
        mBinding.tfTime.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureTimePickerDialog();
            }
        });
    }

    //Configure the TimePickerDialog
    private void configureTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Date dateTime = manageAddMeeting.getTimeFromTimePicker(timePicker, hourOfDay, minute);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String result = formatter.format(dateTime);
                time = result;
                mBinding.etTime.setText(result);
                mBinding.tfTime.setErrorEnabled(false);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener,lastSelectedHour, lastSelectedMinute, is24HView);
        timePickerDialog.show();
    }

    //Save the chosen time
    private void saveTheTime() {
        manageAddMeeting.checkTheTimeFormat(mBinding.etTime, mBinding.tfTime);
        manageAddMeeting.checkOpenedRooms(date, time, openedRooms, AddMeetingActivity.this);
        manageAddMeeting.allRoomsReserved(openedRooms, AddMeetingActivity.this);
        manageAddMeeting.enableTheRoomSelection(mBinding.etTime, mBinding.etDate, mBinding.etTime, mBinding.buttonRooms, openedRooms);
        enableButtonSave();
    }

    //Select a room
    private void selectARoom() {
        manageAddMeeting.selectARoom(mBinding.buttonRooms, openedRooms);
        enableButtonSave();
    }

    //Select a duration
    private void selectADuration() {
        manageAddMeeting.chooseADuration(mBinding.buttonDuration, durationList);
        enableButtonSave();
    }

    //Autocomplete the EditText for collaborators
    private void configureAutoCompleteCollaborator() {
        AutoCompleteTextView autoCompleteTextView = mBinding.etEnter;
        List<String> collaborators =  mApiService.getDummyCollaboratorList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, collaborators);
        autoCompleteTextView.setAdapter(adapter);
    }

    //Add collaborators to collaborator list
    private void addCollaborator() {
        manageAddMeeting.typeCollaborator(mBinding.etEnter, mBinding.btSaveCollaborator);
        manageAddMeeting.saveCollaborator(mBinding.btSaveCollaborator, mBinding.etEnter, collaboratorList, mBinding.chipGroup);
        enableButtonSave();
    }

    //Check if topic is filled
    private void addTopic() {
        mBinding.etTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonSave();
            }
        });
    }

    //The button save is enabled only when all fields are filled
    private void enableButtonSave() {
        if(mBinding.etDate.getText().toString().isEmpty() ||
                mBinding.etTime.getText().toString().isEmpty() ||
                mBinding.buttonDuration.getText().toString().contains("définir") ||
                mBinding.buttonRooms.getText().toString().contains("définir") ||
                mBinding.chipGroup.getChildCount() < 2 ||
                mBinding.etTopic.getText().toString().isEmpty()) {
            mBinding.buttonSave.setEnabled(false);
        }
        else {
            mBinding.buttonSave.setEnabled(true);
            mBinding.buttonSave.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    //Save the meeting and go back to the main activity
    private void saveMeeting() {
        mBinding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Room mRoom : openedRooms){
                    if(mBinding.buttonRooms.getText().toString().equals(mRoom.getName())){
                        room = mRoom;
                    }
                }
                //manageAddMeeting.saveTheRoom(openedRooms, mBinding.buttonRooms, room);
                Meeting meeting = new Meeting(
                "Réunion " + (mApiService.getMeetingList().size()+1),
                room,
                date,
                time,
                mBinding.buttonDuration.getText().toString(),
                collaboratorList,
                mBinding.etTopic.getText().toString()
                );
                mApiService.addMeeting(meeting);
                finish();
            }
        });
    }
}