package com.google.codeu.chatme.view.adapter;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.User;

import java.util.List;

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
     * @param conversation conversation to display messages of
     */
    void openMessageActivity(Conversation conversation);

    /**
     * Launches CreateGroupActivity for conversation passed as param
     *
     * @param conversation conversation object
     */
    void openCreateGroupActivity(Conversation conversation);
}
