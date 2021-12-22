package com.anne.linger.mareu.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;

import com.anne.linger.mareu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingActivity extends AppCompatActivity {

    @BindView(R.id.tb)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        ButterKnife.bind(this);
        configureToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds item to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.menu);
    }

    @OnClick(R.id.fab)
    public void addMeeting(){
        AddMeetingActivity.navigate(this);
    }

}