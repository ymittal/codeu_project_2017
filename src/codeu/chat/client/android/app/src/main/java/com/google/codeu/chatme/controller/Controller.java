package com.google.codeu.chatme.controller;

import android.util.Log;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Controller {

    private static final String TAG = Controller.class.getName();

    private static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    /**
     * Saves a new user to Firebase real-time database
     *
     * @param id   id of {@link User}
     * @param name display name of {@link User}
     */
    public static void addUser(final String id, String name) {
        User newUser = new User(name);

        mRootRef.child("users").child(id).setValue(newUser, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.w(TAG, "addUser:failure " + databaseError.getMessage());
                } else {
                    Log.i(TAG, "addUser:success " + id);
                }
            }
        });
    }

    /**
     * Saves a new conversation to Firebase real-time database
     *
     * @param owner id of {@link Conversation} owner creator
     */
    public static void addConversation(String owner) {
        Conversation newConversation = new Conversation(owner);

        // generates unique id for conversation to be saved
        final String newConversationId = mRootRef.child("conversations").push().getKey();

        mRootRef.child("conversations").child(newConversationId).setValue(newConversation,
                new DatabaseReference.CompletionListener() {

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w(TAG, "addConversation:failure " + databaseError.getMessage());
                        } else {
                            Log.i(TAG, "addConversation:success " + newConversationId);
                        }
                    }
                });
    }

}
