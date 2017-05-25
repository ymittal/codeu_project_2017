package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.Message;
import com.google.codeu.chatme.model.PublicUserDetails;
import com.google.codeu.chatme.utility.network.RetrofitBuilder;
import com.google.codeu.chatme.view.adapter.MessagesAdapterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Following MVP design pattern, this class encapsulates the functionality to
 * handle messaging in a particular conversation
 *
 * @see MessagesInteractor for documentation on interface methods
 */
public class MessagesPresenter implements MessagesInteractor {

    private static final String TAG = MessagesPresenter.class.getName();

    private DatabaseReference mRootRef;

    private final MessagesAdapterView view;

    public MessagesPresenter(MessagesAdapterView view) {
        this.view = view;
    }

    @javax.annotation.PostConstruct
    public void postConstruct() {
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void loadMessages(String conversationId) {
        Query conversationsQuery = mRootRef.child("messages")
                .orderByChild("conversation").equalTo(conversationId);

        conversationsQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Message> messages = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    message.setId(data.getKey());
                    messages.add(message);
                    Log.d(TAG, "loadMessages:onDataChange:messageId:" + message.getId());
                }

                view.setMessagesOnView(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "loadMessages:failure " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void sendMessage(Message newMessage) {
        final String newMessageId = mRootRef.child("messages").push().getKey();

        mRootRef.child("messages").child(newMessageId).setValue(newMessage,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w(TAG, "addMessage:failure " + databaseError.getMessage());
                        } else {
                            Log.i(TAG, "addMessage:success " + newMessageId);
                        }
                    }
                });
    }

    @Override
    public void getParticipantDetails(List<String> participants) {
        RetrofitBuilder.getService().getDetailsFromIds(participants)
                .enqueue(new Callback<HashMap<String, PublicUserDetails>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, PublicUserDetails>> call,
                                           Response<HashMap<String, PublicUserDetails>> response) {
                        Log.i(TAG, "getParticipantDetails:retrieved "
                                + String.valueOf(response.body().size()));

                        view.setParticipantDetailsMap(response.body());
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, PublicUserDetails>> call,
                                          Throwable t) {
                        Log.e(TAG, "getParticipantDetails:failure " + t.getMessage());
                    }
                });
    }
}