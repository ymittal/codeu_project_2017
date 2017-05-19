package com.google.codeu.chatme.view.create;


public interface CreateGroupView {

    /**
     * Launches MessageActivity for the specific group conversation
     */
    void openMessageActivity();

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
