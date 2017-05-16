package com.google.codeu.chatme.view.create;


import com.google.codeu.chatme.view.message.MessagesActivity;

public interface CreateGroupView {

    /**
     * Launches {@link MessagesActivity} for the specific conversation
     *
     * @param conversationId id of conversation to display messages of
     */
    void openMessageActivity(String conversationId);


    /**
     * Sets group's avatar picture
     *
     * @param downloadUrl url of group picture
     */
    void setGroupPicture(String downloadUrl);
    /**
     * Shows progress loader with the given message
     *
     * @param messsage resource Id of string message to display
     */
    void showProgressDialog(int messsage);

    /**
     * Hides progress loader
     */
    void hideProgressDialog();

}
