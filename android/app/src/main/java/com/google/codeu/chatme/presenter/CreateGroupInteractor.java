package com.google.codeu.chatme.presenter;


import android.net.Uri;

import com.google.codeu.chatme.model.Conversation;

/**
 * In accordance with Interactor Design Pattern, this interface provides function(s)
 * which can be used to access Firebase database for data related to creating conversations for
 * the current user
 */
public interface CreateGroupInteractor {


    /**
     * Uploads group avatar to Firebase Storage and then updates Database
     * with the picture download url
     *
     * @param data {@link Uri} containing new profile picture
     * @param conversationId ID for group conversation
     */
    void uploadGroupPictureToStorage(Uri data, String conversationId);
}
