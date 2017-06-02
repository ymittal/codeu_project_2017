package com.google.codeu.chatme.view.adapter;

import com.google.codeu.chatme.model.Message;
import com.google.codeu.chatme.model.PublicUserDetails;

import java.util.ArrayList;
import java.util.HashMap;

public interface MessagesAdapterView {

    /**
     * Updates the view to display the messages
     *
     * @param messages list of messages
     */
    void setMessagesOnView(ArrayList<Message> messages);

    /**
     * Resets the map from participant ids to their public details
     *
     * @param body map from participant ids to details
     */
    void setParticipantDetailsMap(HashMap<String, PublicUserDetails> body);

    /**
     * Sets title of MessagesActivity for one-on-one and group conversations
     */
    void setScreenTitle();
}
