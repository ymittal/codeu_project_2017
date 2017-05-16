package com.google.codeu.chatme.utility;

import org.junit.Assert;
import org.junit.Test;

public class DateTimeUtilTest {

    private static final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

    @Test
    public void getReadableTime() throws Exception {
        // May 11, 2017 12:00:00 AM
        String result = DateTimeUtil.getReadableTime(1494475200000L);
        String expected = "12:00 AM";
        Assert.assertEquals(expected, result);

        // May 11, 2017 10:45:00 AM
        result = DateTimeUtil.getReadableTime(1494513900000L);
        expected = "10:45 AM";
        Assert.assertEquals(expected, result);

        // May 11, 2017 2:45:00 AM
        result = DateTimeUtil.getReadableTime(1494485100000L);
        expected = "2:45 AM";
        Assert.assertEquals(expected, result);

        // Dec 31, 1969 7:00:00 PM
        result = DateTimeUtil.getReadableTime(0L);
        expected = "7:00 PM";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void isYesterday() throws Exception {
        // today
        long timeInMillis = System.currentTimeMillis();
        boolean result = DateTimeUtil.isYesterday(timeInMillis);
        boolean expected = false;
        Assert.assertEquals(expected, result);

        // yesterday
        timeInMillis -= MILLIS_IN_DAY;
        result = DateTimeUtil.isYesterday(timeInMillis);
        expected = true;
        Assert.assertEquals(expected, result);

        // day before yesterday
        timeInMillis -= MILLIS_IN_DAY;
        result = DateTimeUtil.isYesterday(timeInMillis);
        expected = false;
        Assert.assertEquals(expected, result);

    }

    @Test
    public void getReadableDate() throws Exception {
        // May 11, 2017 12:00:00 AM
        String result = DateTimeUtil.getReadableDate(1494475200000L);
        String expected = "May 11, 2017";
        Assert.assertEquals(result, expected);

        // Dec 31, 1969 11:59:59 PM (GMT)
        result = DateTimeUtil.getReadableDate(-1L);
        expected = "Dec 31, 1969";
        Assert.assertEquals(result, expected);
    }

}