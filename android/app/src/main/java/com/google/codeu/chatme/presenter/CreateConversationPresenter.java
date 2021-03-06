package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;

public class CreateConversationPresenter implements CreateConversationInteractor {

    private static final String TAG = CreateConversationPresenter.class.getName();

    private final UserListAdapter view;

    private DatabaseReference mRootRef;

    public CreateConversationPresenter(UserListAdapter view) {
        this.view = view;
    }

    @javax.annotation.PostConstruct
    public void postConstruct() {
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void addConversation(final Conversation conversation) {
        final String conversationId = mRootRef.child("conversations").push().getKey();
        mRootRef.child("conversations").child(conversationId).setValue(conversation,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w(TAG, "addConversation:failure " + databaseError.getMessage());
                        } else {
                            Log.i(TAG, "addConversation:success " + conversationId);
                            conversation.setId(conversationId);
                            view.openMessageActivity(conversation);
                        }
                    }
                });
    }

    @Override
    public void openConversationMessages(final Conversation conversation) {
        mRootRef.child("conversations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashSet participantsSet = new HashSet(conversation.getParticipants());

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Conversation conv = data.getValue(Conversation.class);

                    // opens Messages if participants same as those of an existing conversation
                    HashSet participants = new HashSet(conv.getParticipants());
                    if (participantsSet.equals(participants)) {
                        conv.setId(data.getKey());
                        view.openMessageActivity(conv);
                        return;
                    }
                }

                // none of the other conversations have the same participants
                addConversation(conversation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "openConversationMessages:failure " + databaseError.getMessage());
            }
        });
    }
}
