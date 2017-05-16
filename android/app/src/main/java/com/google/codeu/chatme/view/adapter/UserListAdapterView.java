package com.google.codeu.chatme.view.adapter;

import android.content.Context;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.view.create.CreateGroupActivity;
import com.google.codeu.chatme.view.message.MessagesActivity;

import java.util.List;

/**
 * An interface to handle presenter-delegated actions in order to
 * update list of users in {@link UserListAdapter}
 */
public interface UserListAdapterView {
    /**
     * Resets the list of users in {@link UserListAdapter}
     *
     * @param users new list of users
     */
    public void setUserList(List<User> users);

    /**
     * Launches {@link MessagesActivity} for the specific conversation
     *
     * @param conversationId id of conversation to display messages of
     */
    void openMessageActivity(String conversationId);

    /**
     * Launches {@link CreateGroupActivity} with a conversation consisting
     * of the selected participants
     *
     *  @param conversationId id of conversation being created
     */
    void openCreateGroupActivity(String conversationId);

}
