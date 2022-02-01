package com.anne.linger.mareu.controller.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.controller.adapters.ListMeetingAdapter;
import com.anne.linger.mareu.databinding.ActivityMeetingBinding;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.event.DeleteMeetingEvent;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;
import com.anne.linger.mareu.utils.DateTimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Main activity to display meetings in a recycler view
 */
public class MeetingActivity extends AppCompatActivity {

    private static ActivityMeetingBinding mBinding;
    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final RoomApiService mRoomApiService = DIRoom.getRoomApiService();
    private static final DateTimeUtils mDateTimeUtils = new DateTimeUtils();
    private static RecyclerView mRecyclerView;
    private static List<Meeting> mMeetingList;
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initList();
        addMeeting();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
    }

    //Init the list of meetings
    private static void initList() {
        //Recover the meeting list
        mMeetingList = mApiService.getMeetingList();
        //Items of the RecyclerView filled with the meeting list
        mRecyclerView.setAdapter(new ListMeetingAdapter(mMeetingList));
    }

    //Init the list of meetings with a filter
    private void initList(List<Meeting> meetingList) {
        //Recover the meeting list filtered
        mMeetingList = meetingList;
        //Items of the RecyclerView filled with the meeting list filtered
        mRecyclerView.setAdapter(new ListMeetingAdapter(mMeetingList));
    }

    //Configure the filter menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds item to the action bar if it is present
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        isFilterActive(false);
        return true;
    }

    //Manage the filter icon color according to active or not
    private void isFilterActive (Boolean active) {
        if (active) {
            mBinding.tbMain.setOverflowIcon(getDrawable(R.drawable.ic_baseline_filter_list_yellow));
        }
        else {
            mBinding.tbMain.setOverflowIcon(getDrawable(R.drawable.ic_baseline_filter_list_24));
        }
    }

    //Listener on the filter items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.date_filter:
                dateFilter();
                return true;
            case R.id.room_filter:
                roomDialog();
                return true;
            case R.id.reset_filter:
                resetFilters();
                isFilterActive(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Configure a DatePickerDialog to filter by date
    private void dateFilter() {

        //Get current date
        final Calendar calendar = Calendar.getInstance();
        this.lastSelectedYear = calendar.get(Calendar.YEAR);
        this.lastSelectedMonth = calendar.get(Calendar.MONTH);
        this.lastSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        //Listener for the date selection
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                isFilterActive(true);

                //Init the list with filter meetings
                initList(mApiService.getMeetingListByDate(date));

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDay = dayOfMonth;
            }
        };

        //Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(this, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);
        datePickerDialog.show();
    }

    private void roomDialog() {
        //Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salles de réunion");

        //Add a radio button list filled with room list
        List<String> roomList = new ArrayList<>();
        for (Room room : mRoomApiService.getRoomList()) {
            roomList.add(room.getName());
        }
        roomList.add("Toutes les salles");
        String[] rooms = new String[11];
        roomList.toArray(rooms);
        int checkedItem = 10;

        //Listener for the room selection
        builder.setSingleChoiceItems(rooms, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item <= 9) {
                    initList(mApiService.getMeetingListByRoom(mRoomApiService.getRoomList().get(item).getName()));
                    isFilterActive(true);
                }
                else {
                    resetRoomDialog();
                }
            }
        });

        //Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetRoomDialog();
            }
        });

        //Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Reset room selection
    private void resetRoomDialog() {
        initList(mApiService.getMeetingList());
        isFilterActive(false);
    }

    //Reset all the filters
    private void resetFilters() {
        initList();
    }

    @Subscribe
    //Delete a meeting
    public void deleteMeeting(DeleteMeetingEvent event) {
        Log.e("tag", event.meeting.toString());
        mApiService.deleteMeeting(event.meeting);
        initList();
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