package com.anne.linger.mareu.utils;

import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Useful methods for date and time
 */
public class DateTimeUtils {

    //Recover the date from the DatePickerDialog
    public java.util.Date getDateFromDatePicker(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    //Recover the time from the TimePickerDialog
    public java.util.Date getTimeFromTimePicker(TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        return calendar.getTime();
    }
}
