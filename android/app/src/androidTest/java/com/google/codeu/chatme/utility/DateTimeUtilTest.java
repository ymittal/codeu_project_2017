package com.google.codeu.chatme.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.view.message.MessagesActivity;

import org.junit.Before;
import org.junit.Rule;
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
        // 59.999 seconds
        long time = DateTimeUtil.SECS_IN_MINUTE * DateTimeUtil.MILLIS_IN_SECOND - 1;
        String result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        String expected = context.getString(R.string.less_than_a_min);
        assertEquals(expected, result);

        // 60 seconds
        time = DateTimeUtil.SECS_IN_MINUTE * DateTimeUtil.MILLIS_IN_SECOND;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = context.getResources().getQuantityString(R.plurals.number_mins, 1);
        assertEquals(expected, result);

        // 2 mins
        time = 2 * DateTimeUtil.SECS_IN_MINUTE * DateTimeUtil.MILLIS_IN_SECOND;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = context.getResources().getQuantityString(R.plurals.number_mins, 2, 2);
        assertEquals(expected, result);

        // 1 hour
        time = DateTimeUtil.SECS_IN_HOUR * DateTimeUtil.MILLIS_IN_SECOND;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = context.getResources().getQuantityString(R.plurals.number_hrs, 1);
        assertEquals(expected, result);

        // 23 hrs 59 mins 59 seconds 999 milliseconds
        time = DateTimeUtil.SECS_IN_DAY * DateTimeUtil.MILLIS_IN_SECOND - 1;
        result = DateTimeUtil.convertToReadableTimePeriod(time, context);
        expected = context.getResources().getQuantityString(R.plurals.number_hrs, 23, 23);
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

    @Test
    public void testConvertToReadableTimePeriodWhenTimeExceedsDay() {
        try {
            long time = DateTimeUtil.SECS_IN_MINUTE * DateTimeUtil.MILLIS_IN_SECOND;
            DateTimeUtil.convertToReadableTimePeriod(time, context);
            fail();
        } catch (AssertionError error) {
        }
    }
}