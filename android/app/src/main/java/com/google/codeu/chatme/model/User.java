package com.google.codeu.chatme.model;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.utility.DateTimeUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public final class User {

    private String id;
    private String fullName;
    private String username;
    private long timeCreated;
    private String photoUrl;
    private long lastSeen;
    private boolean isOnline;

    public User() {
        this.timeCreated = System.currentTimeMillis();
    }

    public User(String username) {
        this.username = username;
        this.timeCreated = System.currentTimeMillis();
    }

    public User(String id, String username, String fullName, String photoUrl) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setIsOnline(boolean online) {
        isOnline = online;
    }

    public boolean getIsOnline() {
        return this.isOnline;
    }

    @Exclude
    public String getReadableLastSeen(Context context) {
        long differenceInMillis = System.currentTimeMillis() - lastSeen;
        assert differenceInMillis > 0;
        if (lastSeen == 0) {
            return "";
        }

        if (DateUtils.isToday(lastSeen)) {
            // today
            String readableTime = DateTimeUtil.getReadableTime(lastSeen);
            return String.format(context.getString(R.string.last_seen_at), readableTime);
        } else if ((differenceInMillis / DateTimeUtil.MILLIS_IN_SECOND) < DateTimeUtil.SECS_IN_DAY) {
            // less than 24 hrs
            String period = DateTimeUtil.convertToReadableTimePeriod(differenceInMillis, context);
            return String.format(context.getString(R.string.last_seen_ago), period);
        } else {
            // 24 hrs or more
            String readableDate = DateTimeUtil.getReadableDate(lastSeen);
            return String.format(context.getString(R.string.last_seen_on), readableDate);
        }
    }
}
