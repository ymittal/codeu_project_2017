package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateConversationPresenter implements CreateConversationInteractor {

    private static final String TAG = ConversationsPresenter.class.getName();
    /**
     * {@link UserListAdapter} reference to update list of conversations
     */
    private final UserListAdapter view;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    /**
     * Constructor to accept a reference to a recycler view adapter to bind
     * conversation data to views
     *
     * @param view {@link UserListAdapter} reference
     */
    public CreateConversationPresenter(UserListAdapter view) {
        this.view = view;
    }

    /**
     * Adds conversation to Firebase DB and returns new conversation Id
     *
     * @param conversation return conversationId
     */
    public String addConversation(Conversation conversation) {
        /* TODO: Check for existing conversation with same participants before adding to DB */

        String conversationId = newConversationId();
        mRootRef.child("conversations").child(conversationId).setValue(conversation);
        Log.i(TAG, "addConversation:success");

        return conversationId;
    }

    /**
     * Generates a new conversation ID
     *
     * @return conversationId
     */
    public String newConversationId() {
        return mRootRef.child("conversations").push().getKey();
    }
}
