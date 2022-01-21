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
import com.anne.linger.mareu.controller.ManageOpenedRooms;
import com.anne.linger.mareu.databinding.ActivityAddMeetingBinding;
import com.anne.linger.mareu.databinding.ChipEntryBinding;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;
import com.anne.linger.mareu.utils.PopupUtils;
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
    private static final PopupUtils popupUtils = new PopupUtils();
    private static final ManageOpenedRooms MANAGE_OPENED_ROOMS = new ManageOpenedRooms();
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

    //Recover the date from the DatePickerDialog
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker, int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    //Convert String date to LocalDate
    private void convertDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatter = formatter.withLocale(Locale.FRANCE);
        date = LocalDate.parse(dateString, formatter);
    }

    //Display the date in the EditText
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

                if (!stringDate.matches("([0-9]{2}/([0-9]{2})/([0-9]{4}))")) {
                    mBinding.etDate.setError(getText(R.string.date_error));
                }
                else{
                    mBinding.tfDate.setErrorEnabled(false);
                    convertDate(stringDate);
               }
                MANAGE_OPENED_ROOMS.checkOpenedRooms(date, time, openedRooms, AddMeetingActivity.this);
               MANAGE_OPENED_ROOMS.allRoomsReserved(openedRooms, AddMeetingActivity.this);
               ManageOpenedRooms.enableButtonRoom(mBinding.etDate, mBinding.etTime, mBinding.buttonRooms, openedRooms);
                enableButtonSave();
           }
       });
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

    //Recover the time from the TimePickerDialog
    public static java.util.Date getTimeFromTimePicker(TimePicker timePicker, int hourOfDay, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        return calendar.getTime();
    }

    //Display the time in the EditText
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
                time = mBinding.etTime.getText().toString();
                if (!time.matches("[0-9]{2}[:][0-9]{2}")) {
                    mBinding.etTime.setError(getText(R.string.time_error));
                }
                else{
                    mBinding.tfTime.setErrorEnabled(false);
                }
                MANAGE_OPENED_ROOMS.checkOpenedRooms(date, time, openedRooms, AddMeetingActivity.this);
                MANAGE_OPENED_ROOMS.allRoomsReserved(openedRooms, AddMeetingActivity.this);
                ManageOpenedRooms.enableButtonRoom(mBinding.etDate, mBinding.etTime, mBinding.buttonRooms, openedRooms);
                enableButtonSave();
            }
        });
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
        PopupMenu popupMenuRoom = popupUtils.createPopupMenu(this, mBinding.buttonRooms);
        Menu menuRoom = popupUtils.createMenu(popupMenuRoom);
        popupUtils.addRoomItems(openedRooms, menuRoom);
        popupMenuRoom.show();

        popupMenuRoom.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
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
        PopupMenu popupMenuDuration = popupUtils.createPopupMenu(this, mBinding.buttonDuration);
        Menu menuDuration = popupUtils.createMenu(popupMenuDuration);
        popupUtils.addStringItems(durationList, menuDuration);
        popupMenuDuration.show();

        popupMenuDuration.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mBinding.buttonDuration.setText(menuItem.getTitle());
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

    //Autocomplete the EditText for collaborators
    private void configureAutoCompleteCollaborator() {
        AutoCompleteTextView autoCompleteTextView = mBinding.etEnter;
        List<String> collaborators =  mApiService.getDummyCollaboratorList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, collaborators);
        autoCompleteTextView.setAdapter(adapter);
    }

    //Add collaborators to EditText
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

    //Add a chip for the collaborator added
    private void addNewChip() {
        String chipText = mBinding.etEnter.getText().toString();
        LayoutInflater inflater = LayoutInflater.from(this);
        Chip newChip = (Chip) inflater.inflate(R.layout.chip_entry, mBinding.chipGroup, false);
        newChip.setText(chipText);
        newChip.setCloseIconVisible(true);
        newChip.setOnCloseIconClickListener(this);
        mBinding.chipGroup.addView(newChip);
    }

    //Remove a chip
    @Override
    public void onClick(View view) {
        Chip chip = (Chip) view;
        String collaboratorToRemove = chip.getText().toString();
        collaboratorList.remove(collaboratorToRemove);
        mBinding.chipGroup.removeView(chip);
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

                Log.e("tag", date.toString());
                Log.e("tag", duration);

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