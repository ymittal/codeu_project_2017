package com.google.codeu.chatme.presenter;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.codeu.chatme.R;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.codeu.chatme.view.create.CreateGroupView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateGroupPresenter implements CreateGroupInteractor {

    private static final String TAG = ConversationsPresenter.class.getName();
    /**
     * {@link UserListAdapter} reference to update list of conversations
     */
    private final CreateGroupView view;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    /**
     * Constructor to accept a reference to a recycler view adapter to bind
     * conversation data to views
     *
     * @param view {@link CreateGroupView} reference
     */
    public CreateGroupPresenter(CreateGroupView view) {
        this.view = view;
    }


    @SuppressWarnings("VisibleForTests")
    @Override
    public void uploadGroupPictureToStorage(final Uri data, final String conversationId) {
        view.showProgressDialog(R.string.progress_upload_pic);
        StorageReference filepath = FirebaseStorage.getInstance().getReference()
                .child("group-pics").child(FirebaseUtil.getCurrentUserUid());

        filepath.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        Log.i(TAG, "uploadGroupPicture:success:downloadUrl " + downloadUrl);
                        view.setGroupPicture(downloadUrl);
                        updateGroupPhotoUrl(downloadUrl, conversationId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "uploadGroupPicture:failure");
                    }
                });
    }

    @Override
    public void setGroupName(final String groupName, String conversationId) {
        if (groupName.isEmpty()) {
            return;
        }

        mRootRef.child("conversations").child(conversationId)
                .child("groupName").setValue(groupName, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e(TAG, "updateGroupName:failure " + databaseError.getMessage());
                } else {
                    Log.i(TAG, "updateGroupName:success set to " + groupName);
                }
            }
        });
        view.openMessageActivity(conversationId);
    }

    /**
     * Updates logged-in user's profile pic storage url
     *
     * @param downloadUri    new profile pic download url
     * @param conversationId ID for group conversation
     */
    private void updateGroupPhotoUrl(String downloadUri, String conversationId) {
        mRootRef.child("conversations").child(conversationId)
                .child("photoUrl").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "updateGroupPhotoUrl:success");
                } else {
                    Log.e(TAG, "updateGroupPhotoUrl:failure");
                }
            }
        });
    }
}
