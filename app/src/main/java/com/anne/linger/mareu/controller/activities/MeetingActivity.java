package com.anne.linger.mareu.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.Menu;
import android.view.View;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.databinding.ActivityMeetingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.OnClick;

public class MeetingActivity extends AppCompatActivity {

    private ActivityMeetingBinding mBinding;

    //Configure the UI
    private void initUi() {
        mBinding = ActivityMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureToolbar();
        addMeeting();
    }

    //Configure the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds item to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Configure the Toolbar
    private void configureToolbar(){
        setSupportActionBar(mBinding.tbMain);
        mBinding.tbMain.inflateMenu(R.menu.menu);
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