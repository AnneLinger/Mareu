package com.anne.linger.mareu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.anne.linger.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.notNullValue;

import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.anne.linger.mareu.controller.activities.MeetingActivity;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;
import com.anne.linger.mareu.utils.DeleteViewAction;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Instrumented tests for the meetings
 */

@RunWith(AndroidJUnit4.class)
public class MeetingInstrumentedTest {

    private MeetingApiService mApiService;
    private final RoomApiService mRoomApiService = DIRoom.getRoomApiService();

    private final List<String> mCollaboratorList = Arrays.asList("test@lamzone.com", "test2@lamzone.com");
    private final Meeting mMeetingTest = new Meeting("Réunion test", mRoomApiService.getRoomList().get(0), Calendar.getInstance().getTime(), "14:00", "1 heure", mCollaboratorList, "Test");

    @Rule
    public ActivityScenarioRule<MeetingActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MeetingActivity.class);

    @Before
    public void setUp() {
        ActivityScenario<MeetingActivity> mActivity = mActivityScenarioRule.getScenario();
        MatcherAssert.assertThat(mActivity, notNullValue());

        mApiService = DIMeeting.getMeetingApiService();
        MatcherAssert.assertThat(mApiService, notNullValue());
    }

    //Display a meeting and its required details
    @Test
    public void displayAMeetingAndItsDetails() {
        mApiService.addMeeting(mMeetingTest);
        onView(ViewMatchers.withId(R.id.im_circle)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.tv_time)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.tv_topic)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.rv_collaborators)).check(matches(isDisplayed()));
        mApiService.deleteMeeting(mMeetingTest);
    }

    //Navigate to add a new meeting when the user clicks on the the add button
    @Test
    public void navigateToTheAddMeetingActivity() {
        onView(ViewMatchers.withId(R.id.fab_add)).perform(click());
        onView(ViewMatchers.withId(R.id.cl_add_meeting)).check(matches(isDisplayed()));
    }

    //Delete a meeting when the user clicks on the delete button
    @Test
    public void deleteAMeeting() {
        mApiService.addMeeting(mMeetingTest);
        int ITEMS_COUNT = 0;
        onView(ViewMatchers.withId(R.id.rv_meeting)).check(withItemCount(ITEMS_COUNT +1));
        onView(ViewMatchers.withId(R.id.rv_meeting)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        onView(ViewMatchers.withId(R.id.rv_meeting)).check(withItemCount(ITEMS_COUNT));
    }

    //Show a DatePickerDialog to choose a date when the user clicks on filter by date
    @Test
    public void showDatePickerToFilterTheListByDate() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(ViewMatchers.withText("Filtrer par date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(matches(isDisplayed()));
    }

    //Show the room list to choose a room when the user clicks on filter by room
    @Test
    public void showRoomListToFilterTheListByRoom() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(ViewMatchers.withText("Filtrer par salle")).perform(click());
        onView(withText("Salles de réunion")).check(matches(isDisplayed()));
    }
}