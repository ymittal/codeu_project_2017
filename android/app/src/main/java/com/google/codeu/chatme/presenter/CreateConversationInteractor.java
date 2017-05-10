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
     * @param conversation
     */
    public void addConversation(Conversation conversation);
    /**
     * Gets the current user's ID
     *
     * @return userId
     */
    public String getUserId();

    /**
     * Generates a new conversation ID
     *
     * @return conversationId
     */
    public String newConversationId();
}
