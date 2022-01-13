package com.anne.linger.mareu.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.controller.adapter.ListMeetingAdapter;
import com.anne.linger.mareu.databinding.ActivityMeetingBinding;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.services.meeting.MeetingApiService;

import java.util.List;

public class MeetingActivity extends AppCompatActivity {

    private static ActivityMeetingBinding mBinding;
    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static RecyclerView mRecyclerView;
    private static List<Meeting> mMeetingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initList();
        addMeeting();
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    //Configure the UI
    private void initUi() {
        mBinding = ActivityMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        configureToolbar();
        initRecyclerView();
    }

    //Configure the Toolbar
    private void configureToolbar() {
        setSupportActionBar(mBinding.tbMain);
        mBinding.tbMain.inflateMenu(R.menu.filter_menu);
    }

    ///Init the recycler view
    private void initRecyclerView() {
        mRecyclerView = mBinding.rvMeeting;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //ListMeetingAdapter listMeetingAdapter = new ListMeetingAdapter(mMeetingList);
        //mRecyclerView.setAdapter(listMeetingAdapter);
    }

    //Init the list of meetings
    public static void initList() {
        //Recover the neighbour list or the favorite neighbours
        mMeetingList = mApiService.getMeetingList();
        //Items of the RecyclerView filled with mNeighbourList
        mRecyclerView.setAdapter(new ListMeetingAdapter(mMeetingList));
    }

    //Configure the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds item to the action bar if it is present
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    //Navigate to the AddMeetingActivity
    private void addMeeting() {
        mBinding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeetingActivity.this, AddMeetingActivity.class);
                ActivityCompat.startActivity(MeetingActivity.this, intent, null);
            }
        });
    }
}