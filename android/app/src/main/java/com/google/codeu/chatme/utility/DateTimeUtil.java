package com.google.codeu.chatme.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;

/**
 * A utility class to facilitate date time formatting
 */
public class DateTimeUtil {

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
}
