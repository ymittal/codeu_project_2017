package com.google.codeu.chatme.model;

import android.text.format.DateUtils;

import com.google.codeu.chatme.utility.DateTimeUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public final class Message implements Serializable {

    private String id;
    private String author;
    private String conversation;
    private String content;
    private long timeCreated;

    public Message() {
    }

    public Message(String author, String content, String conversation) {
        this.author = author;
        this.content = content;
        this.conversation = conversation;
        this.timeCreated = System.currentTimeMillis();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getConversation() {
        return conversation;
    }

    public String getContent() {
        return content;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    @Exclude
    public String getReadableTime() {
        long time = this.timeCreated;
        if (DateUtils.isToday(time)) {
            return DateTimeUtil.getReadableTime(time);
        } else if (DateTimeUtil.isYesterday(time)) {
            return "Yesterday";
        } else {
            return DateTimeUtil.getReadableDate(time);
        }
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
