package com.google.codeu.chatme.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.tabs.ProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * Following MVP design pattern, this class encapsulates the functionality to
 * perform user profile changes and log out using Firebase
 *
 * @see LoginActivityInteractor for documentation on interface methods
 */
public class ProfilePresenter implements ProfileInteractor {

    private static final String TAG = ProfilePresenter.class.getName();

    private DatabaseReference mRootRef;

    private final ProfileView view;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public ProfilePresenter(final ProfileView view) {
        this.view = view;
    }

    @javax.annotation.PostConstruct
    public void postConstruct() {
        this.mRootRef = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();

        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    view.openLoginActivity();
                }
            }
        };
    }

    @Override
    public void getUserProfile() {
        mRootRef.child("users").child(FirebaseUtil.getCurrentUserUid())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        view.setUserProfile(user);
                        Log.i(TAG, "getUserProfile:success profile data loaded");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "getUserProfile:failure could not load profile data");
                    }
                });
    }

    @SuppressWarnings("VisibleForTests")
    @Override
    public void uploadProfilePictureToStorage(final Uri data) {
        view.showProgressDialog(R.string.progress_upload_pic);

        StorageReference filepath = FirebaseStorage.getInstance().getReference()
                .child("profile-pics").child(FirebaseUtil.getCurrentUserUid());

        filepath.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        Log.i(TAG, "updateProfilePicture:success:downloadUrl " + downloadUrl);

                        view.setProfilePicture(downloadUrl);
                        updateUserPhotoUrl(downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "updateProfilePicture:failure");
                        view.hideProgressDialog();
                        view.makeToast(e.getMessage());
                    }
                });
    }

    /**
     * Updates logged-in user's profile pic storage url
     *
     * @param downloadUri new profile pic download url
     */
    private void updateUserPhotoUrl(String downloadUri) {
        mRootRef.child("users").child(FirebaseUtil.getCurrentUserUid())
                .child("photoUrl").setValue(downloadUri);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(downloadUri))
                .build();

        FirebaseUtil.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "updateUserPhotoUrl:success");
                        } else {
                            Log.e(TAG, "updateUserPhotoUrl:failure");
                        }
                    }
                });
    }

    @Override
    public void signOut() {
        final DatabaseReference userRef = mRootRef.child("users").child(FirebaseUtil.getCurrentUserUid());

        userRef.child("isOnline").setValue(Boolean.FALSE, new CompletionListener() {
            @Override
            public void onComplete(DatabaseError err, DatabaseReference ref) {
                if (err == null) {
                    userRef.child("lastSeen").setValue(ServerValue.TIMESTAMP, new CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError err, DatabaseReference ref) {
                            if (err == null) {
                                userRef.child("instanceId").removeValue(new CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError err, DatabaseReference ref) {
                                        if (err == null) {
                                            mAuth.signOut();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    view.makeToast(R.string.sign_out_failure);
                }
            }
        });
    }

    @Override
    public void updateUser(String fullName, String username, String password) {
        updateFullName(fullName);
        updateUserName(username);
        updatePassword(password);
    }

    /**
     * Updates logged-in user's full name
     *
     * @param fullName new full name
     */
    private void updateFullName(final String fullName) {
        if (fullName.isEmpty()) {
            return;
        }

        mRootRef.child("users").child(FirebaseUtil.getCurrentUserUid())
                .child("fullName").setValue(fullName, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e(TAG, "updateFullName:failure " + databaseError.getMessage());
                } else {
                    Log.i(TAG, "updateFullName:success changed to " + fullName);
                }
            }
        });
    }

    /**
     * Updates logged-in user's username
     *
     * @param username new username
     */
    private void updateUserName(final String username) {
        if (username.isEmpty()) {
            return;
        }

        mRootRef.child("users").child(FirebaseUtil.getCurrentUserUid())
                .child("username").setValue(username, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e(TAG, "updateUserName:failure " + databaseError.getMessage());
                } else {
                    Log.i(TAG, "updateUserName:success changed to " + username);
                }
            }
        });
    }

    /**
     * Updates logged-in user's password
     *
     * @param password new password
     */
    public void updatePassword(String password) {
        if (password.isEmpty()) {
            return;
        }
        view.showProgressDialog(R.string.progress_update_pwd);

        FirebaseUtil.getCurrentUser().updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        view.hideProgressDialog();
                        if (task.isSuccessful()) {
                            Log.i(TAG, "updatePassword:success");
                            view.makeToast(R.string.toast_pwd_name);
                        } else {
                            Log.e(TAG, "updatePassword:failure");
                            view.makeToast(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public void deleteAccount() {
        mRootRef.child("users").child(FirebaseUtil.getCurrentUserUid())
                .removeValue();

        FirebaseUtil.getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "deleteAccount:success account deleted");
                            view.openLoginActivity();
                        } else {
                            Log.e(TAG, "deleteAccount:failure account could not be deleted");
                            view.makeToast(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public void setAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void removeAuthStateListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
