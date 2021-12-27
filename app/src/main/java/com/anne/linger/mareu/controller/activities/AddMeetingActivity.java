package com.anne.linger.mareu.controller.activities;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.databinding.ActivityAddMeetingBinding;
import com.anne.linger.mareu.databinding.ActivityMeetingBinding;
import com.anne.linger.mareu.di.DI;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*Created by Anne Linger on 20/12/2021.
*/
public class AddMeetingActivity extends AppCompatActivity {

    private static ActivityAddMeetingBinding mBinding;
    private static final MeetingApiService mApiService = DI.getMeetingApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initClickOnRoomMenu();
        initClickOnCollaboratorMenu();
        saveMeeting();
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
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

    private void initClickOnRoomMenu() {
        mBinding.buttonRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRoomMenu();
            }
        });
    }

    private void showRoomMenu() {
        PopupMenu popup = new PopupMenu(this, this.mBinding.buttonRooms);
        popup.inflate(R.menu.room_menu);

        //Menu menu = popup.getMenu();

        popup.show();
    }

    private void initClickOnCollaboratorMenu() {
        mBinding.buttonCollaborators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCollaboratorMenu();
            }
        });
    }

    private void showCollaboratorMenu() {
        PopupMenu popup = new PopupMenu(this, this.mBinding.buttonCollaborators);
        popup.inflate(R.menu.collaborator_menu);

        //Menu menu = popup.getMenu();

        popup.show();
    }

    private void saveMeeting() {
        mBinding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> myList = new ArrayList<>();
                myList.add("adam@lamzone.com");
                myList.add("caroline@lamzone.com");
                myList.add("karl@lamzone.com");

                Meeting fakeMeeting = new Meeting(
                        "Fake réunion",
                        new Room("Salle A", false),
                        "14:00",
                        "15:00",
                        "Fake topic",
                        myList
                        );
                mApiService.addMeeting(fakeMeeting);
            }
        });
    }

}
