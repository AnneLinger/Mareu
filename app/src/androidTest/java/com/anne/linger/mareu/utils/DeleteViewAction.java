package com.anne.linger.mareu.utils;

import android.content.res.Resources;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.anne.linger.mareu.R;

import org.hamcrest.Matcher;

/**
 * Created by Anne Linger on 31/01/2022.
 */
public class DeleteViewAction implements ViewAction {
    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return Resources.getSystem().getString(R.string.delete_description);
    }

    @Override
    public void perform(UiController uiController, View view) {
        View button = view.findViewById(R.id.im_delete);
        // Maybe check for null
        button.performClick();
    }
}
