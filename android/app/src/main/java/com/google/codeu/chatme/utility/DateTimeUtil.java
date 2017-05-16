package com.google.codeu.chatme.utility;

import android.content.Context;

import com.google.codeu.chatme.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;

/**
 * A utility class to facilitate date time formatting
 */
public class DateTimeUtil {

    public static final int MILLIS_IN_SECOND = 1000;
    public static final int SECS_IN_DAY = 24 * 60 * 60;
    public static final int SECS_IN_HOUR = 60 * 60;
    public static final int SECS_IN_MINUTE = 60;

    /**
     * Converts time to human readable time of a day (e.g. "9:03 AM")
     *
     * @param timeInMillis time in milliseconds
     * @return readable time of a day
     */
    public static String getReadableTime(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        return sdf.format(new Date(timeInMillis));
    }

    /**
     * Checks if the time corresponds to the previous day
     *
     * @param timeInMillis time in milliseconds
     * @return true if time is from the previous day
     */
    public static boolean isYesterday(long timeInMillis) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(DAY_OF_YEAR, -1);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);

        return yesterday.get(YEAR) == cal.get(YEAR)
                && yesterday.get(DAY_OF_YEAR) == cal.get(DAY_OF_YEAR);
    }

    /**
     * Converts time to human readable date (e.g. "SEP 12, 2008")
     *
     * @param timeInMillis time in milliseconds
     * @return readable date
     */
    public static String getReadableDate(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        return sdf.format(new Date(timeInMillis));
    }

    /**
     * Converts time to human readable time period (e.g. "an hr" or "2 mins")
     * I know
     *
     * @param milliseconds time in milliseconds
     * @param context      context to obtain strings
     * @return readable period
     */
    public static String convertToReadableTimePeriod(long milliseconds, Context context) {
        assert milliseconds > 0;

        long seconds = milliseconds / MILLIS_IN_SECOND;
        if (seconds < SECS_IN_MINUTE) {
            return context.getString(R.string.less_than_a_min);
        } else if (seconds < SECS_IN_HOUR) {
            int mins = (int) seconds / SECS_IN_MINUTE;
            return context.getResources().getQuantityString(R.plurals.number_mins, mins, mins);
        } else {
            int hours = (int) seconds / SECS_IN_HOUR;
            return context.getResources().getQuantityString(R.plurals.number_hrs, hours, hours);
        }
    }
}
