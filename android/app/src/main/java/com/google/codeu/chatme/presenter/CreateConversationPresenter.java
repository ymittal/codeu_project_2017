package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateConversationPresenter implements CreateConversationInteractor {

    private static final String TAG = ConversationsPresenter.class.getName();

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    /**
     * {@link UserListAdapter} reference to update list of conversations
     */
    private final UserListAdapter view;

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
     * Adds conversation to Firebase DB
     *
     * @param conversation
     */
    public void addConversation(Conversation conversation) {
        /* TODO: Check for existing conversation with same participants before adding to DB */

        mRootRef.child("conversations").child(conversation.getId()).setValue(conversation);
        Log.i(TAG, "addConversation:success");
    }

    /**
     * Generates a new conversation ID
     *
     * @return conversationId
     */
    public String newConversationId() {
        String conversationId = mRootRef.child("conversations").push().getKey();
        return conversationId;
    }

    /**
     * Gets the current user's ID
     *
     * @return userId
     */
    public String getUserId() {
        String userId = mAuth.getCurrentUser().getUid();
        return userId;

    }
}
