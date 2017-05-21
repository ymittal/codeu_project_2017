package com.google.codeu.chatme.common.view;

public interface BaseFragmentView {

    /**
     * Shows progress loader
     *
     * @param messsage resource Id of string message to display
     */
    void showProgressDialog(int messsage);

    /**
     * Hides progress loader
     */
    void hideProgressDialog();

    /**
     * Creates a short toast message
     *
     * @param message message to be toasted
     */
    void makeToast(String message);

    /**
     * Creates a short toast message
     *
     * @param messageId resource Id of message to be toasted
     */
    void makeToast(int messageId);

}
