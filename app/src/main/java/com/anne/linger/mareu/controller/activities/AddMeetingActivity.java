package com.anne.linger.mareu.controller.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.databinding.ActivityAddMeetingBinding;
import com.anne.linger.mareu.databinding.ChipEntryBinding;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
*Created by Anne Linger on 20/12/2021.
*/
@RequiresApi(api = Build.VERSION_CODES.O)
public class AddMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    private static ActivityAddMeetingBinding mBinding;
    private static ChipEntryBinding mChipBinding;
    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final RoomApiService mRoomApiService = DIRoom.getRoomApiService();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private boolean is24HView = true;
    private int lastSelectedHour;
    private int lastSelectedMinute;
    private LocalDate date;
    private String time;
    private List<String> durationList = mApiService.getDummyDurationList();
    private String duration;
    private List<Room> openedRooms = mRoomApiService.getRoomList();
    private Room room;
    private List<String> collaboratorList = new ArrayList<>();
    private String name = "Réunion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        selectDate();
        selectTime();
        initClickOnRoomMenu();
        initClickOnDurationMenu();
        saveMeeting();
        configureAutoCompleteCollaborator();
        addCollaborator();
        addTopic();
        addDate();
        addTime();
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
        mChipBinding = ChipEntryBinding.inflate(getLayoutInflater());
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

    private void configureDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Date date = getDateFromDatePicker(datePicker, year, monthOfYear, dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String result = formatter.format(date);
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

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker, int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private void convertDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatter = formatter.withLocale(Locale.FRANCE);
        date = LocalDate.parse(dateString, formatter);
    }

    private void addDate() {
       mBinding.etDate.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               String stringDate = mBinding.etDate.getText().toString();
               Log.e("tag", mBinding.etDate.getText().toString());

               if (!stringDate.matches("([0-9]{2}/([0-9]{2})/([0-9]{4}))")) {
                    mBinding.etDate.setError(getText(R.string.date_error));
               }
                else{
                    mBinding.tfDate.setErrorEnabled(false);
                    convertDate(stringDate);
                            Log.e("tag", date.toString());
                    checkOpenedRooms();
               }
               enableButtonRoom();
               enableButtonSave();
           }
       });
    }

    private void selectTime() {
        mBinding.tfTime.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureTimePickerDialog();
            }
        });
    }

    private void configureTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Date time = getTimeFromTimePicker(timePicker, hourOfDay, minute);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String result = formatter.format(time);
                mBinding.etTime.setText(result);
                mBinding.tfTime.setErrorEnabled(false);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener,lastSelectedHour, lastSelectedMinute, is24HView);
        timePickerDialog.show();
    }

    public static java.util.Date getTimeFromTimePicker(TimePicker timePicker, int hourOfDay, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        return calendar.getTime();
    }

    private void addTime() {
        mBinding.etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String stringTime = mBinding.etTime.getText().toString();
                if (!stringTime.matches("[0-9]{2}[:][0-9]{2}")) {
                    mBinding.etTime.setError(getText(R.string.time_error));
                }
                else{
                    mBinding.tfTime.setErrorEnabled(false);
                }
                enableButtonRoom();
                enableButtonSave();
            }
        });
    }

    //Show the popup menu for the duration when button is clicked
    private void initClickOnDurationMenu() {
        mBinding.buttonDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDurationMenu();
            }
        });
    }

    //Configure the popup menu for the duration
    private void configureDurationMenu() {
        PopupMenu popup = new PopupMenu(this, this.mBinding.buttonDuration);
        Menu durationMenu = popup.getMenu();

        for (String duration : durationList){
            durationMenu.add(duration);
        }

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mBinding.buttonDuration.setText(menuItem.getTitle());
                enableButtonRoom();
                enableButtonSave();
                for (String mDuration : durationList){
                    if(menuItem.getTitle().toString().equals(mDuration)){
                        duration = mDuration;
                    }
                }
                return true;
            }
        });
    }

    private void enableButtonRoom() {
        if(mBinding.etDate.getText().toString().isEmpty() ||
                mBinding.etTime.getText().toString().isEmpty() ||
                mBinding.buttonDuration.getText().toString().contains("définir"))
        {
            mBinding.buttonRooms.setEnabled(false);
        }
        else {
            mBinding.buttonRooms.setEnabled(true);
        }
    }

    private void checkOpenedRooms() {
        for (Meeting meeting : mApiService.getMeetingList()){
            if (meeting.getDate().equals(date)){
                Log.e("tag", date.toString());
                Log.e("tag", mApiService.getMeetingList().get(0).getDate().toString());
                openedRooms.remove(meeting.getRoom());
            }
        }
        if (openedRooms.isEmpty()) {
            Toast.makeText(AddMeetingActivity.this, "Toutes les salles sont réservées, merci de choisir une autre date", Toast.LENGTH_SHORT).show();
        }
    }

    //Show the popup menu for the rooms when button is clicked
    private void initClickOnRoomMenu() {
        mBinding.buttonRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureRoomMenu();
            }
        });
    }

    //Configure the popup menu for the rooms
    private void configureRoomMenu() {
        PopupMenu popup = new PopupMenu(this, this.mBinding.buttonRooms);
        Menu roomMenu = popup.getMenu();

        //checkOpenedRooms();

        for (Room room : openedRooms){
            roomMenu.add(room.getName());
        }

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mBinding.buttonRooms.setText(menuItem.getTitle());
                enableButtonSave();
                for (Room mRoom : openedRooms){
                    if(menuItem.getTitle().toString().equals(mRoom.getName())){
                        room = mRoom;
                    }
                }
                return true;
            }
        });
    }

    private void configureAutoCompleteCollaborator() {
        AutoCompleteTextView autoCompleteTextView = mBinding.etEnter;
        List<String> collaborators =  mApiService.getDummyCollaboratorList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, collaborators);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void addCollaborator() {
        mBinding.etEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBinding.btSaveCollaborator.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String stringCollaborator = mBinding.etEnter.getText().toString();
                if (!stringCollaborator.matches("^(.+)@lamzone.com")) {
                    mBinding.etEnter.setError(getText(R.string.collaborator_error));
                }
                else{
                    mBinding.btSaveCollaborator.setEnabled(true);
                }
            }
        });

        mBinding.btSaveCollaborator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewChip();
                String newCollaborator = mBinding.etEnter.getText().toString();
                collaboratorList.add(newCollaborator);
                mBinding.etEnter.clearFocus();
                mBinding.etEnter.setText(null);
                mBinding.etEnter.setError(null, null);
                enableButtonSave();
            }
        });
    }

    private void addNewChip() {
        String chipText = mBinding.etEnter.getText().toString();
        LayoutInflater inflater = LayoutInflater.from(this);
        Chip newChip = (Chip) inflater.inflate(R.layout.chip_entry, mBinding.chipGroup, false);
        newChip.setText(chipText);
        newChip.setCloseIconVisible(true);
        newChip.setOnCloseIconClickListener(this);
        mBinding.chipGroup.addView(newChip);
    }

    @Override
    public void onClick(View view) {
        Chip chip = (Chip) view;
        String collaboratorToRemove = chip.getText().toString();
        collaboratorList.remove(collaboratorToRemove);
        mBinding.chipGroup.removeView(chip);
    }

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

    private void saveMeeting() {
        mBinding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag", date.toString());
                Log.e("tag", mBinding.etTime.getText().toString());
                Log.e("tag", duration);
                Log.e("tag", collaboratorList.toString());

                        Meeting meeting = new Meeting(
                        "Réunion " + (mApiService.getMeetingList().size()+1),
                        room,
                        date,
                        mBinding.etTime.getText().toString(),
                        duration,
                        collaboratorList,
                        mBinding.etTopic.getText().toString()
                );
                mApiService.addMeeting(meeting);
                finish();
            }
        });
    }
}