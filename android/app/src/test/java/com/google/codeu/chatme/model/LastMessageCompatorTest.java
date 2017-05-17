package com.google.codeu.chatme.model;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LastMessageCompatorTest {

    private Conversation.LastMessageCompator compator;
    private Conversation conversation1;
    private Conversation conversation2;

    @Before
    public void setUp() throws Exception {
        compator = new Conversation.LastMessageCompator();

        conversation1 = new Conversation();
        conversation1.setLastMessage(new Message());

        conversation2 = new Conversation();
        conversation2.setLastMessage(new Message());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCompare() {
        conversation1.getLastMessage().setTimeCreated(100L);
        conversation2.getLastMessage().setTimeCreated(95L);
        int result = compator.compare(conversation1, conversation2);
        int expected = -1;
        Assert.assertEquals(expected, result);

        conversation1.getLastMessage().setTimeCreated(100L);
        conversation2.getLastMessage().setTimeCreated(100L);
        result = compator.compare(conversation1, conversation2);
        expected = 0;
        Assert.assertEquals(expected, result);

        conversation1.getLastMessage().setTimeCreated(100L);
        conversation2.getLastMessage().setTimeCreated(105L);
        result = compator.compare(conversation1, conversation2);
        expected = 1;
        Assert.assertEquals(expected, result);
    }
}