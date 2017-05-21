package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.adapter.UserListAdapterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Following MVP design pattern, this class encapsulates the functionality to
 * store and retrieve data related to the current users from Firebase
 * database
 *
 * @see UserInteractor for documentation of interface methods
 */
public class UserPresenter implements UserInteractor {

    private static final String TAG = UserPresenter.class.getName();

    private DatabaseReference mRootRef;

    private final UserListAdapterView view;

    public UserPresenter(UserListAdapterView view) {
        this.view = view;
    }

    @javax.annotation.PostConstruct
    public void postConstruct() {
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void loadUsers() {
        mRootRef.child("users").orderByChild("fullName").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!data.getKey().equals(FirebaseUtil.getCurrentUserUid())) {
                        User user = data.getValue(User.class);
                        user.setId(data.getKey());
                        users.add(user);
                        Log.d(TAG, "loadUsers:onDataChange:userId:" + user.getId());
                    }
                }

                view.setUserList(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "loadUsers:failure " + databaseError.getMessage());
            }
        });
    }
}
