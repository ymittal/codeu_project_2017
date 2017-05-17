package com.google.codeu.chatme.presenter;

import com.google.codeu.chatme.model.Conversation;

/**
 * In accordance with Interactor Design Pattern, this interface provides function(s)
 * which can be used to access Firebase database for data related to creating conversations for
 * the current user
 */
public interface CreateConversationInteractor {

    /**
     * Creates new 2-user conversation between current logged in user and a second
     * user of their choosing
     *
     * @param conversation conversation object to add to database
     */
    void addConversation(Conversation conversation);

    /**
     * First checks whether any of the existing conversations in the database have the same
     * participants and then, accordingly, adds a new conversation or opens
     * {@link com.google.codeu.chatme.view.message.MessagesActivity} for the existing one
     *
     * @param conversation conversation object to open the messages of
     */
    void openConversationMessages(Conversation conversation);
}
