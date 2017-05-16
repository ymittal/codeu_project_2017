package com.google.codeu.chatme.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public final class User {

    private String id;
    private String fullName;
    private String username;
    private long timeCreated;
    private String photoUrl;
    private long lastSeen;

    /**
     * List of active devices (strings are NOT device ids)
     */
    private HashMap<String, Boolean> connections = new HashMap<>();

    public User() {
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

    public HashMap<String, Boolean> getConnections() {
        return connections;
    }

    @Exclude
    public boolean isOnline() {
        if (connections == null) {
            return false;
        }
        return connections.size() > 0;
    }

    @Exclude
    public String getReadableLastSeen() {
        if (lastSeen == 0) {
            return "";
        }

        // TODO: use DateTimeUtil to figure out "x time ago"
        return String.valueOf(lastSeen);
    }
}
