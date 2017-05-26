package com.google.codeu.chatme.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@IgnoreExtraProperties
public final class Conversation implements Serializable {

    public static final int MIN_CONV_PARTICIPANTS = 2;

    private String id;
    private String owner;
    private long timeCreated;
    private Message lastMessage;
    private boolean isGroup;
    private final List<String> participants = new ArrayList<>();
    private String photoUrl;
    private String groupName;
    //public HashMap<String, Boolean> readLastMessage  = new HashMap<String, Boolean>(); //del??
    private HashMap<String, String> lastReadMessage  = new HashMap<>();
    private HashSet<String> currentlyViewing = new HashSet<>();

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
     * Updates the last message seen by a user
     * @param uid string uid of a user
     * @param mid string representing a message id
     */
    public void setLastRead(String uid, String mid) {
        lastReadMessage.put(uid, mid);
    }

    /**
     * Creating a new conversation defaults all users that are not the author
     * to false, or "have not seen last message" if they are not currently viewing the
     * conversation, will be called whenever a user sends a message to a conversation
     */
    public void resetReadLastMessage(String author) {
        List<String> participants = getParticipants();
        for(String uid : participants) {
            if(isCurrentlyViewing(uid)) {
                this.lastReadMessage.put(uid, lastMessage.getId());
            }
        }
    }

    /**
     * Returns if a user has or has not read the last message of a conversation
     * @param uid the id of the user in question
     * @return true if the user has read the last message, and false if not
     */
    public boolean hasReadLastMessage(String uid) {
        if(lastReadMessage.get(uid) == null) {
            return false;
        } else {
            return lastReadMessage.get(uid).equals(lastMessage.getId());
        }
    }

    /**
     * Either adds or removes user from the currently viewing HashSet
     * @param uid String of the user id of the user who is being regarded
     * @param isViewing boolean value representing if a user is or is not
     *                  currently viewing a conversation
     */
    public void setCurrentlyViewing(String uid, boolean isViewing) {
        if(isViewing) {
            currentlyViewing.add(uid);
        } else {
            currentlyViewing.remove(uid);
        }
    }

    public boolean isCurrentlyViewing(String uid) {
        return currentlyViewing.contains(uid);
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
