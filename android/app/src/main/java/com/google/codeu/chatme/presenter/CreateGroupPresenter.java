package com.google.codeu.chatme.presenter;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.view.create.CreateGroupView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateGroupPresenter implements CreateGroupInteractor {

    private static final String TAG = CreateGroupPresenter.class.getName();

    private final CreateGroupView view;

    private DatabaseReference mRootRef;

    /**
     * @param view view object
     */
    public CreateGroupPresenter(CreateGroupView view) {
        this.view = view;
    }

    @javax.annotation.PostConstruct
    public void postConstruct() {
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @SuppressWarnings("VisibleForTests")
    @Override
    public void uploadGroupPictureToStorage(final Uri data, final String conversationId) {
        view.showProgressDialog(R.string.progress_upload_pic);
        StorageReference filepath = FirebaseStorage.getInstance().getReference()
                .child("group-pics").child(conversationId);

        filepath.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        Log.i(TAG, "uploadGroupPicture:success:downloadUrl " + downloadUrl);
                        updateGroupPhotoUrl(downloadUrl, conversationId);
                        view.openMessageActivity(conversationId);
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
    public void addGroupConversation(final Conversation conversation, final Uri picData) {
        if (conversation.getGroupName().isEmpty()) {
            return;
        }

        final String conversationId = mRootRef.child("conversations").push().getKey();
        mRootRef.child("conversations").child(conversationId).setValue(conversation,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w(TAG, "addGroupConversation:failure " + databaseError.getMessage());
                        } else {
                            Log.i(TAG, "addGroupConversation:success " + conversationId);
                            if (picData != null) {
                                uploadGroupPictureToStorage(picData, conversationId);
                            } else {
                                view.openMessageActivity(conversationId);
                            }
                        }
                    }
                });
    }

    @Override
    public void updateGroupPhotoUrl(String downloadUri, String conversationId) {
        mRootRef.child("conversations").child(conversationId).child("photoUrl")
                .setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
