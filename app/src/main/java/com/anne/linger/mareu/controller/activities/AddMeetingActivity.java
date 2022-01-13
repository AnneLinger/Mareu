package com.anne.linger.mareu.controller.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;

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

import java.util.ArrayList;
import java.util.List;

/**
*Created by Anne Linger on 20/12/2021.
*/
public class AddMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    private static ActivityAddMeetingBinding mBinding;
    private static ChipEntryBinding mChipBinding;
    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final RoomApiService mRoomApiService = DIRoom.getRoomApiService();
    private Room room;
    private List<String> collaboratorList = new ArrayList<>();
    private String name = "Réunion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initClickOnRoomMenu();
        saveMeeting();
        configureAutoCompleteCollaborator();
        addCollaborator();
        addTopic();
        addDate();
        addTime();
        //mApiService.clearCollaboratorList();
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
               if (!stringDate.matches("[0-9]{2}[/][0-9]{2}[/][0-9]{4}")) {
                    mBinding.etDate.setError(getText(R.string.date_error));
               }
                else{
                    mBinding.tfDate.setErrorEnabled(false);
               }
                enableButtonSave();
           }
       });
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
                String stringDate = mBinding.etTime.getText().toString();
                if (!stringDate.matches("[0-9]{2}[:][0-9]{2}")) {
                    mBinding.etTime.setError(getText(R.string.time_error));
                }
                else{
                    mBinding.tfTime.setErrorEnabled(false);
                }
                enableButtonSave();
            }
        });
    }

    //Show the popup menu when button is clicked
    private void initClickOnRoomMenu() {
        mBinding.buttonRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureRoomMenu();
            }
        });
    }

    //Configure the popup menu
    private void configureRoomMenu() {
        PopupMenu popup = new PopupMenu(this, this.mBinding.buttonRooms);
        Menu roomMenu = popup.getMenu();

        for (Room room : mRoomApiService.getRoomList()){
            roomMenu.add(room.getName());
        }

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mBinding.buttonRooms.setText(menuItem.getTitle());
                enableButtonSave();
                switch (menuItem.getTitle().toString()) {
                    case "Salle beige" :
                        room = mRoomApiService.getRoomList().get(0);
                        break;
                    case "Salle bleue" :
                        room = mRoomApiService.getRoomList().get(1);
                        break;
                    case "Salle grise" :
                        room = mRoomApiService.getRoomList().get(2);
                        break;
                    case "Salle jaune" :
                        room = mRoomApiService.getRoomList().get(3);
                        break;
                    case "Salle orange" :
                        room = mRoomApiService.getRoomList().get(4);
                        break;
                    case "Salle rose" :
                        room = mRoomApiService.getRoomList().get(5);
                        break;
                    case "Salle rouge" :
                        room = mRoomApiService.getRoomList().get(6);
                        break;
                    case "Salle turquoise" :
                        room = mRoomApiService.getRoomList().get(7);
                        break;
                    case "Salle verte" :
                        room = mRoomApiService.getRoomList().get(8);
                        break;
                    case "Salle violette" :
                        room = mRoomApiService.getRoomList().get(9);
                        break;
                    default:
                        room = null;
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
        mBinding.chipGroup.removeView(chip);
        String collaboratorToRemove = mBinding.etEnter.getText().toString();
        collaboratorList.remove(collaboratorToRemove);
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

                Meeting meeting = new Meeting(
                        "Réunion " + (mApiService.getMeetingList().size()+1),
                        room,
                        "14:00",
                        "15:00",
                        collaboratorList,
                        mBinding.tfTopic.getEditText().getText().toString()
                );
                mApiService.addMeeting(meeting);
                finish();
            }
        });
    }
}