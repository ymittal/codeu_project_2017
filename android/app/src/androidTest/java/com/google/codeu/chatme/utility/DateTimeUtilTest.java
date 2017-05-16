package com.google.codeu.chatme.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DateTimeUtilTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void convertToReadableTimePeriod() throws Exception {
        // 59 seconds 999 milliseconds
        long time = DateTimeUtil.SECS_IN_MINUTE * DateTimeUtil.MILLIS_IN_SECOND - 1;
        String result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        String expected = "less than a minute";
        assertEquals(expected, result);

        // 1 minute
        time = DateTimeUtil.SECS_IN_MINUTE * DateTimeUtil.MILLIS_IN_SECOND;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = "a min";
        assertEquals(expected, result);

        // 59 minutes 59 seconds 999 milliseconds
        time = DateTimeUtil.SECS_IN_HOUR * DateTimeUtil.MILLIS_IN_SECOND - 1;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = "59 mins";
        assertEquals(expected, result);

        // 1 hour
        time = DateTimeUtil.SECS_IN_HOUR * DateTimeUtil.MILLIS_IN_SECOND;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = "an hr";
        assertEquals(expected, result);

        // 23 hrs 59 minutes 59 seconds 999 milliseconds
        time = DateTimeUtil.SECS_IN_DAY * DateTimeUtil.MILLIS_IN_SECOND - 1;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = "23 hrs";
        assertEquals(expected, result);
    }

    @Test
    public void convertToReadableTimePeriodWhenTimeZero() {
        try {
            DateTimeUtil.convertToReadableTimePeriod(0, context);
            fail();
        } catch (AssertionError error) {
        }
    }
}