package com.google.codeu.chatme.view.adapter;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.PublicUserDetails;

import java.util.HashMap;
import java.util.List;

interface ConversationListAdapterView {

    /**
     * Resets the list of conversations in {@link ConversationListAdapter}
     *
     * @param conversations new list of conversations
     */
    void setChatList(List<Conversation> conversations);

    /**
     * Resets the map from participant ids to their details
     *
     * @param map map from participant ids to details
     */
    void setParticipantDetailsMap(HashMap<String, PublicUserDetails> map);
}
