package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.firebase.database.DatabaseError;
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

    @Override
    public void addConversation(Conversation conversation) {
        /* TODO: Check for existing conversation with same participants before adding to DB */

        final String conversationId = mRootRef.child("conversations").push().getKey();
        mRootRef.child("conversations").child(conversationId).setValue(conversation,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w(TAG, "addConversation:failure " + databaseError.getMessage());
                        } else {
                            Log.i(TAG, "addConversation:success " + conversationId);
                            view.openMessageActivity(conversationId);
                        }
                    }
                });
    }

    @Override
    public void addGroupConversation(Conversation conversation) {


        final String conversationId = mRootRef.child("conversations").push().getKey();
        mRootRef.child("conversations").child(conversationId).setValue(conversation,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w(TAG, "addGroupConversation:failure " + databaseError.getMessage());
                        } else {
                            Log.i(TAG, "addGroupConversation:success " + conversationId);
                            view.openCreateGroupActivity(conversationId);
                        }
                    }
                });
    }
}
