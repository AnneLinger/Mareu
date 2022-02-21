package com.anne.linger.mareu.controller.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.controller.manager.ManageAddMeeting;
import com.anne.linger.mareu.databinding.ActivityAddMeetingBinding;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;
import com.anne.linger.mareu.utils.DateTimeUtils;
import com.anne.linger.mareu.utils.PopupUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Activity to add a new meeting to the recycler view
 */
public class AddMeetingActivity extends ManageAddMeeting {

    private ActivityAddMeetingBinding mBinding;
    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final RoomApiService mRoomApiService = DIRoom.getRoomApiService();
    private static final ManageAddMeeting mManageAddMeeting = new ManageAddMeeting();
    private static final DateTimeUtils mDateTimeUtils = new DateTimeUtils();
    private static final PopupUtils mPopupUtils = new PopupUtils();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    public Date date;
    private String time;
    private final List<String> timeList = mApiService.getDummyTimeList();
    private final List<Room> openedRooms = mRoomApiService.getRoomList();
    private Room room;
    private final List<String> collaboratorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        selectDate();
        saveTheDate();
        saveTheTime();
        selectARoom();
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
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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
    private void selectDate() {
        mBinding.tfDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDatePickerDialog();
            }
        });

        //Get the current date
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
                date = mDateTimeUtils.getDateFromDatePicker(year, monthOfYear, dayOfMonth);
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
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(this, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);
        datePickerDialog.show();
    }

    //Save the date
    public void saveTheDate() {
        mBinding.etDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mManageAddMeeting.checkTheDateFormat(mBinding.etDate, mBinding.tfDate);
                mManageAddMeeting.checkOpenedRooms(date, time, openedRooms);
                mManageAddMeeting.allRoomsReserved(openedRooms, AddMeetingActivity.this);
                mBinding.buttonRooms.setEnabled(!mBinding.etDate.getText().toString().isEmpty() &&
                        !mBinding.buttonTime.getText().toString().contains("Heure") && !openedRooms.isEmpty());
            }
        });
    }

    //Save the time
    private void saveTheTime() {
        assert mBinding.buttonTime != null;
        mBinding.buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = mPopupUtils.createPopupMenu(mBinding.buttonTime.getContext(), mBinding.buttonTime);
                Menu menu = mPopupUtils.createMenu(popupMenu);
                for (String time : timeList) {
                    menu.add(time);
                }
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mBinding.buttonTime.setText(menuItem.getTitle());
                        time = mBinding.buttonTime.getText().toString();
                        mManageAddMeeting.checkOpenedRooms(date, time, openedRooms);
                        mManageAddMeeting.allRoomsReserved(openedRooms, AddMeetingActivity.this);
                        mBinding.buttonRooms.setEnabled(!mBinding.etDate.getText().toString().isEmpty() &&
                                !mBinding.buttonTime.getText().toString().contains("Heure") && !openedRooms.isEmpty());
                        enableButtonSave();
                        return true;
                    }
                });
            }
        });
    }

    //Select a room
    private void selectARoom() {
        mManageAddMeeting.selectARoom(mBinding.buttonRooms, openedRooms);
        enableButtonSave();
    }

    //Autocomplete the EditText for collaborators
    private void configureAutoCompleteCollaborator() {
        AutoCompleteTextView autoCompleteTextView = mBinding.etEnter;
        List<String> collaborators = mApiService.getDummyCollaboratorList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, collaborators);
        autoCompleteTextView.setAdapter(adapter);
    }

    //Add collaborators to collaborator list
    private void addCollaborator() {
        mManageAddMeeting.typeCollaborator(mBinding.etEnter, mBinding.btSaveCollaborator);
        mManageAddMeeting.saveCollaborator(mBinding.btSaveCollaborator, mBinding.etEnter, collaboratorList, mBinding.chipGroup);
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
        if (mBinding.etDate.getText().toString().isEmpty() ||
                mBinding.buttonTime.getText().toString().contains(getString(R.string.time)) ||
                mBinding.buttonRooms.getText().toString().contains(getString(R.string.check_room_button)) ||
                mBinding.chipGroup.getChildCount() < 2 ||
                mBinding.etTopic.getText().toString().isEmpty()) {
            mBinding.buttonSave.setEnabled(false);
        } else {
            mBinding.buttonSave.setEnabled(true);
            mBinding.buttonSave.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    //Save the meeting and go back to the main activity
    private void saveMeeting() {
        mBinding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Room mRoom : openedRooms) {
                    if (mBinding.buttonRooms.getText().toString().equals(mRoom.getName())) {
                        room = mRoom;
                    }
                }
                Meeting meeting = new Meeting(
                        getString(R.string.meeting_name) + (mApiService.getMeetingList().size() + 1),
                        room,
                        date,
                        time,
                        collaboratorList,
                        mBinding.etTopic.getText().toString()
                );
                mApiService.addMeeting(meeting);
                finish();
            }
        });
    }
}