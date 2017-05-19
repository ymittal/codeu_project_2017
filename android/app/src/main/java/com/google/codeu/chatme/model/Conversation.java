package com.google.codeu.chatme.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@IgnoreExtraProperties
public final class Conversation {

    public static final int MIN_CONV_PARTICIPANTS = 2;

    private String id;
    private String owner;
    private long timeCreated;
    private Message lastMessage;
    private boolean isGroup;
    private final List<String> participants = new ArrayList<>();
    private String photoUrl;
    private String groupName;

    public Conversation() {
    }

    public Conversation(String owner) {
        this.owner = owner;
        this.participants.add(owner);
        this.timeCreated = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public boolean getIsGroup() {
        return this.isGroup;
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

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Message getLastMessage() {
        return this.lastMessage;
    }

    @Exclude
    public String getLastMessageContent() {
        return this.lastMessage.getContent();
    }

    @Exclude
    public String getReadableLastMessageTime() {
        return this.lastMessage.getReadableTime();
    }

    /**
     * @return reference to a mutable list of participants
     */
    public List<String> getParticipants() {
        return participants;
    }

    public void addParticipant(String participantId) {
        participants.add(participantId);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * A @{@link Comparator} class to order conversation objects according to when their
     * last message was sent (most recent first)
     */
    public static class LastMessageCompator implements Comparator<Conversation> {

        @Override
        public int compare(Conversation conversation, Conversation t1) {
            return Long.compare(t1.getLastMessage().getTimeCreated(),
                    conversation.getLastMessage().getTimeCreated());
        }
    }
}
