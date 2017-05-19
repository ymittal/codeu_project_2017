package com.google.codeu.chatme.view.adapter;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.User;

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
    void setUserList(List<User> users);

    /**
     * Launches MessagesActivity for the specific conversation
     *
     * @param conversationId id of conversation to display messages of
     */
    void openMessageActivity(String conversationId);

    /**
     * Launches CreateGroupActivity for conversation passed as param
     *
     * @param conversation conversation object
     */
    void openCreateGroupActivity(Conversation conversation);
}
