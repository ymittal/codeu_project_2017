package com.google.codeu.chatme.view.create;


import com.google.codeu.chatme.common.view.BaseActivityView;

public interface CreateGroupView extends BaseActivityView {

    /**
     * Launches MessageActivity for the specific group conversation
     *
     * @param conversationId conversation id
     */
    void openMessageActivity(String conversationId);

}
