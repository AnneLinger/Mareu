package com.anne.linger.mareu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static com.anne.linger.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.SystemClock;
import android.widget.Toolbar;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

import com.anne.linger.mareu.controller.activities.MeetingActivity;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.di.DIRoom;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.services.meeting.MeetingApiService;
import com.anne.linger.mareu.services.meeting.NewMeetingApiService;
import com.anne.linger.mareu.services.room.RoomApiService;
import com.anne.linger.mareu.utils.DeleteViewAction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Instrumented tests for the MeetingActivity
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    //private MeetingActivity mMeetingActivity;
    private MeetingApiService mApiService;
    private RoomApiService mRoomApiService = DIRoom.getRoomApiService();
    private List<Meeting> mMeetingList;
    private List<String> mCollaboratorList = Arrays.asList("test@lamzone.com", "test2@lamzone.com");
    private static int ITEMS_COUNT = 0;

    private Meeting mMeetingTest = new Meeting("Réunion test", mRoomApiService.getRoomList().get(0), Calendar.getInstance().getTime(), "14:00", "1 heure", mCollaboratorList, "Test");

    @Rule
    public ActivityScenarioRule<MeetingActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MeetingActivity.class);

    @Before
    public void setUp() {
        ActivityScenario<MeetingActivity> mActivity = mActivityScenarioRule.getScenario();
        MatcherAssert.assertThat(mActivity, notNullValue());

        mApiService = DIMeeting.getMeetingApiService();
        MatcherAssert.assertThat(mApiService, notNullValue());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.anne.linger.mareu", appContext.getPackageName());
    }

    //Navigation to the AddMeetingActivity when the user clicks on the the add button
    @Test
    public void navigateToTheAddMeetingActivity() {
        onView(allOf(ViewMatchers.withId(R.id.fab_add))).perform(click());
        onView(ViewMatchers.withId(R.id.cl_add_meeting)).check(matches(isDisplayed()));
    }

    //Delete a meeting when the user clicks on the delete button
    @Test
    public void deleteAMeeting() {
        mApiService.addMeeting(mMeetingTest);
        onView(allOf(ViewMatchers.withId(R.id.rv_meeting))).check(withItemCount(ITEMS_COUNT+1));
        onView(allOf(ViewMatchers.withId(R.id.rv_meeting))).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        onView(allOf(ViewMatchers.withId(R.id.rv_meeting))).check(withItemCount(ITEMS_COUNT));
    }

    //Show the filter items when the user clicks on the filter button
    @Test
    public void showTheFilterItems() {
        //onView(allOf(withParent(withClassName(is(Toolbar.class.getName()))), isDisplayed())).perform(ViewActions.click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("date")).perform(click());
        onView(ViewMatchers.withId(R.id.date_filter)).check(matches(isDisplayed()));
    }

    //Show a DatePickerDialog when the user clicks on the date filter item

    //Show the list of the rooms when the user clicks on the room filter item

    //Reset the filter when the user clicks on the reset item

    //Filter the list meeting when the user choose a date

    //Filter the list meeting when the user choose a room

}