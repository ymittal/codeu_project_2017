package com.google.codeu.chatme.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.login.LoginView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

/**
 * Following MVP design pattern, this class encapsulates the functionality to
 * perform user sign up and sign in using Firebase
 *
 * @see LoginActivityInteractor for documentation on interface methods
 */
public class LoginActivityPresenter implements LoginActivityInteractor {

    private static final String TAG = LoginActivityPresenter.class.getName();

    private final LoginView view;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LoginActivityPresenter(final LoginView view) {
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
                    refreshInstanceId();
                    setOnlineStatus();
                    view.openTabsActivity();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void refreshInstanceId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                    // forces call to MyFirebaseInstanceIdService#onTokenRefresh
                    FirebaseInstanceId.getInstance().getToken();
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Sets a user's online status when they open the app on their device(s). Also sets up
     * {@link DatabaseReference#onDisconnect()} triggers to update the status and set last seen
     * time when they close the app
     */
    private void setOnlineStatus() {
        final DatabaseReference isOnlineRef = mRootRef.child("users")
                .child(FirebaseUtil.getCurrentUserUid()).child("isOnline");

        final DatabaseReference lastSeenRef = mRootRef.child("users")
                .child(FirebaseUtil.getCurrentUserUid()).child("lastSeen");

        FirebaseDatabase.getInstance().getReference(".info/connected")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            isOnlineRef.setValue(Boolean.TRUE);
                            isOnlineRef.onDisconnect().setValue(Boolean.FALSE);
                            lastSeenRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                        } else {
                            isOnlineRef.setValue(Boolean.FALSE);
                            lastSeenRef.setValue(ServerValue.TIMESTAMP);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "setOnlineStatus:onCancelled " + error.getMessage());
                    }
                });
    }

    @Override
    public void signUp(final String email, String password) {
        boolean isValid = validateInput(email, password);
        if (!isValid) {
            return;
        }

        view.showProgressDialog(R.string.progress_sign_up);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signUp:onComplete:" + task.isSuccessful());
                        view.hideProgressDialog();

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signUp:failure", task.getException());
                            view.makeToast(task.getException().getMessage());
                        } else {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Log.i(TAG, "signUp:success:" + currentUser.getUid());

                            // creates fullName from email address
                            String fullName = email.substring(0, email.indexOf('@'));

                            // saves new user to real-time database
                            User newUser = new User();
                            newUser.setFullName(fullName);
                            addUser(currentUser.getUid(), newUser);
                        }
                    }
                });
    }

    /**
     * Saves a new user to Firebase real-time database
     *
     * @param id      user id
     * @param newUser User object to be saved to database
     */
    private void addUser(final String id, User newUser) {

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

    @Override
    public void signIn(String email, String password) {
        boolean isValid = validateInput(email, password);
        if (!isValid) {
            return;
        }

        view.showProgressDialog(R.string.progress_sign_in);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        view.hideProgressDialog();

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            view.makeToast(task.getException().getMessage());
                        } else {
                            Log.i(TAG, "signInWithEmail:success:"
                                    + mAuth.getCurrentUser().getUid());
                        }
                    }
                });
    }

    /**
     * Validates user email and password for login form
     *
     * @param email    email the user entered
     * @param password password the user entered
     * @return true if the inputs are valid
     */
    public boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            view.setEmailFieldError(R.string.err_et_email);
            return false;
        }
        if (password.isEmpty()) {
            view.setPasswordFieldError(R.string.err_et_password);
            return false;
        }
        return true;
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
