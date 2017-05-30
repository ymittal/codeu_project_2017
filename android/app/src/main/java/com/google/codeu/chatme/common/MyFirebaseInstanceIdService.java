package com.google.codeu.chatme.common;

import android.util.Log;

import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIdService.class.getName();

    /**
     * Generates a new app instance token and saves the same to current user's database
     * node
     */
    @Override
    public void onTokenRefresh() {
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, instanceId);

        if (FirebaseUtil.getCurrentUser() != null) {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mRootRef.child("users").child(FirebaseUtil.getCurrentUserUid())
                    .child("instanceId").setValue(instanceId);
        }
    }

}
