package com.google.codeu.chatme.common.view;


public interface BaseActivityView {

    /**
     * Shows progress loader
     *
     * @param message resource Id of string message to display
     */
    void showProgressDialog(int message);

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
