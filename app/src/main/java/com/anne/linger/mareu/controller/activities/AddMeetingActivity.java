package com.anne.linger.mareu.controller.activities;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.app.Activity;
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
import com.google.android.material.textfield.TextInputLayout;

/**
*Created by Anne Linger on 20/12/2021.
*/
public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initData();
        initClickOnRoomMenu();
        initClickOnCollaboratorMenu();
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

    //Init the data
    private void initData() {

    }

}
