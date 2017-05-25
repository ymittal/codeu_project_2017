package com.google.codeu.chatme.presenter;

import com.google.codeu.chatme.model.Message;

import java.util.List;

interface MessagesInteractor {

    /**
     * Retrieves all messages from the database for the specific conversation
     *
     * @param conversationId id of conversation to load messages of
     */
    void loadMessages(String conversationId);

    /**
     * Saves a new message to Firebase real-time database
     *
     * @param newMessage {@link Message} object to be added
     */
    void sendMessage(Message newMessage);

    /**
     * Retrieves {@link com.google.codeu.chatme.model.PublicUserDetails} for each of the
     * conversation participants to facilitate in displaying their full names
     *
     * @param participants list of conversation participants
     */
    void getParticipantDetails(List<String> participants);
}