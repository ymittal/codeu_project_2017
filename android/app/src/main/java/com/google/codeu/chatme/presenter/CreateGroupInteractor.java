package com.google.codeu.chatme.presenter;


import android.net.Uri;

import com.google.codeu.chatme.model.Conversation;

interface CreateGroupInteractor {

    /**
     * Uploads group avatar to Firebase Storage and then updates Database
     * with the picture download url
     *
     * @param data           {@link Uri} containing new group avatar
     * @param conversationId group conversation id
     */
    void uploadGroupPictureToStorage(Uri data, String conversationId);

    /**
     * Saves new group conversation to database
     *
     * @param conversation conversation object to add to database
     * @param picData      Uri containing group avatar (or null)
     */
    void addGroupConversation(Conversation conversation, Uri picData);

    /**
     * Updates group avatar url in Firebase database
     *
     * @param downloadUri    new group avatar download url
     * @param conversationId group conversation id
     */
    void updateGroupPhotoUrl(String downloadUri, String conversationId);
}
