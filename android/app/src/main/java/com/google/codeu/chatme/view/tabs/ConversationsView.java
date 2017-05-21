package com.google.codeu.chatme.view.tabs;


import com.google.codeu.chatme.common.view.BaseFragmentView;
import com.google.codeu.chatme.view.create.CreateConversationActivity;

public interface ConversationsView extends BaseFragmentView {

    /**
     * Launches {@link CreateConversationActivity}
     */
    void openCreateConversationActivity();

}
