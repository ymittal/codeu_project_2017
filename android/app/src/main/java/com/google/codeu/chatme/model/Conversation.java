package com.google.codeu.chatme.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@IgnoreExtraProperties
public final class Conversation {

    public String id;
    public String owner;
    public long timeCreated;
    public Message lastMessage;

    /**
     * List of participants of a conversation (participants may be added or
     * removed in case "groups" are implemented)
     */
    private final List<String> participants = new ArrayList<>();

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
        // TODO: format string
        return String.valueOf(this.lastMessage.getTimeCreated());
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
