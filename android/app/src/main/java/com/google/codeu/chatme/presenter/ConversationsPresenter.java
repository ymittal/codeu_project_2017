package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.PublicUserDetails;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.utility.network.RetrofitBuilder;
import com.google.codeu.chatme.view.adapter.ConversationListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Following MVP design pattern, this class encapsulates the functionality to
 * store and retrieve data related to current user's conversations from Firebase
 * database
 *
 * @see ConversationsInteractor for documentation of interface methods
 */
public class ConversationsPresenter implements ConversationsInteractor {

    private static final String TAG = ConversationsPresenter.class.getName();

    private final ConversationListAdapter view;

    private DatabaseReference mRootRef;

    public ConversationsPresenter(ConversationListAdapter view) {
        this.view = view;
    }

    @javax.annotation.PostConstruct
    public void postConstruct() {
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void loadConversations() {
        Query conversationsQuery = mRootRef.child("conversations").orderByChild("lastMessage").startAt("");
        conversationsQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Conversation> conversations = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Conversation conv = data.getValue(Conversation.class);
                    if (conv.getParticipants().contains(FirebaseUtil.getCurrentUserUid())) {
                        conv.setId(data.getKey());
                        conversations.add(conv);
                    }
                }

                Collections.sort(conversations, new Conversation.LastMessageComparator());
                Log.i(TAG, "loadConversations:retrieved " + conversations.size());

                // updates list of conversations (and the corresponding views) in adapter
                view.setChatList(conversations);

                getConversationParticipantDetails(conversations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "loadConversations:failure " + databaseError.getMessage());
            }
        });
    }

    /**
     * @param conversations list of conversations
     * @return list of unique conversation participants ids for current user
     */
    private List<String> getUniqueParticipants(ArrayList<Conversation> conversations) {
        Set participants = new HashSet();
        for (Conversation conv : conversations) {
            participants.addAll(conv.getParticipants());
        }
        return new ArrayList<>(participants);
    }

    /**
     * Retrieves a hash map which maps user ids to their details such as full name and pic urls.
     * Uses Retrofit to make an API call to Firebase Functions (backend) by "posting" a list of user
     * ids. Then, updates view by resetting participantDetailsMap
     *
     * @param conversations list of conversations
     */
    private void getConversationParticipantDetails(ArrayList<Conversation> conversations) {
        List<String> participants = getUniqueParticipants(conversations);
        if (participants.size() == 0) {
            Log.i(TAG, "getConversationParticipantDetails: 0 conversations");
            return;
        }

        RetrofitBuilder.getService().getDetailsFromIds(participants)
                .enqueue(new Callback<HashMap<String, PublicUserDetails>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, PublicUserDetails>> call,
                                           Response<HashMap<String, PublicUserDetails>> response) {
                        Log.i(TAG, "getConversationParticipantDetails:retrieved "
                                + String.valueOf(response.body().size()));

                        view.setParticipantDetailsMap(response.body());
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, PublicUserDetails>> call,
                                          Throwable t) {
                        Log.e(TAG, "getConversationParticipantDetails " + t.getMessage());
                    }
                });
    }

}
