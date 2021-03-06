package com.anne.linger.mareu.controller.manager;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.model.Room;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.utils.ChipUtils;
import com.anne.linger.mareu.utils.PopupUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.List;

/**
 * Class to manage creation of a new meeting (for AddMeetingActivity)
 */
public class ManageAddMeeting extends AppCompatActivity {

    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static final PopupUtils mPopupUtils = new PopupUtils();
    private static final ChipUtils mChipUtils = new ChipUtils();

    //Listener on the date EditText to check the date format
    public void checkTheDateFormat(EditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editText.getText().toString().matches("([0-9]{2}/([0-9]{2})/([0-9]{4}))")) {
                    editText.setError("Veuillez entrer un format JJ/MM/AAAA");
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    //Select rooms which are opened according to date and time meeting
    public void checkOpenedRooms(Date date, String time, List<Room> list) {
        for (Meeting meeting : mApiService.getMeetingList()) {
            if (meeting.getDate().equals(date) && meeting.getTime().equals(time)) {
                list.remove(meeting.getRoom());
            } else if (!list.contains(meeting.getRoom())) {
                list.add(meeting.getRoom());
            }
        }
    }

    //Advertising if all rooms are reserved
    public void allRoomsReserved(List<Room> list, Context context) {
        if (list.isEmpty()) {
            Toast.makeText(context, R.string.allRoomsReserved, Toast.LENGTH_SHORT).show();
        }
    }

    //Listener to select a room
    public void selectARoom(Button button, List<Room> list) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = mPopupUtils.createPopupMenu(button.getContext(), button);
                Menu menu = mPopupUtils.createMenu(popupMenu);
                for (Room room : list) {
                    menu.add(room.getName());
                }
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        button.setText(menuItem.getTitle());
                        return true;
                    }
                });
            }
        });
    }

    //Listener to type a collaborator
    public void typeCollaborator(EditText editText, Button button) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editText.getText().toString();
                if (!string.matches("^(.+)@lamzone.com")) {
                    editText.setError("Veuillez entrer un format nom@lamzone.com");
                } else {
                    button.setEnabled(true);
                }
            }
        });
    }

    //Listener to save a collaborator
    public void saveCollaborator(Button button, EditText editText, List<String> list, ChipGroup chipGroup) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = editText.getText().toString();
                list.add(string);
                editText.clearFocus();
                editText.setText(null);
                editText.setError(null, null);
                LayoutInflater inflater = LayoutInflater.from(chipGroup.getContext());
                Chip chip = (Chip) inflater.inflate(R.layout.chip_entry, chipGroup, false);
                chip.setText(string);
                chip.setCloseIconVisible(true);
                chipGroup.addView(chip);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChipUtils.deleteAChipFromAView((Chip) view);
                        mChipUtils.deleteAChipFromAList((Chip) view, list);
                    }
                });
            }
        });
    }
}
