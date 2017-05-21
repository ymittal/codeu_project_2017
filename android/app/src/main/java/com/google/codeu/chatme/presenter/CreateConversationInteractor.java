package com.google.codeu.chatme.presenter;

import com.google.codeu.chatme.model.Conversation;

interface CreateConversationInteractor {

    /**
     * Creates new 2-user conversation between current logged in user and a second
     * user of their choosing
     *
     * @param conversation conversation object to add to database
     */
    void addConversation(Conversation conversation);

    /**
     * Checks whether any existing conversations has the same participants and then, accordingly,
     * adds a new conversation or opens MessageActivity
     *
     * @param conversation conversation object to open the messages of
     */
    void openConversationMessages(Conversation conversation);
}
