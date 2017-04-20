package com.google.codeu.chatme.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.view.tabs.ProfileFragment;
import com.google.codeu.chatme.view.tabs.ProfileView;
import com.google.codeu.chatme.view.login.LoginActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

    /**
     * Sets up the presenter with a reference to the {@link ProfileFragment}.
     * Additionally, adds {@link com.google.firebase.auth.FirebaseAuth.AuthStateListener}
     * to {@link FirebaseAuth} instance to detect changed in user authentication status
     * Refer to {@link ProfilePresenter#postConstruct()}
     *
     * @param view a reference to {@link LoginActivity}
     */

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

    /**
     * Signs out current user
     */
    public void signOut() {
        mAuth.signOut();
        view.openLoginActivity();
    }

    /**
     * Updates current user's profile based on provided paramaters
     *
     * @param fullName user's full name
     * @param username user's username
     * @param password user's password
     */
    public void updateUser(String fullName, String username, String password) {
        boolean isValid = validateInput(fullName, username, password);
        if (!isValid) {
            return;
        }
    }

    /**
     * Validates user email and password for login form
     *
     * @param fullName    email the user entered
     * @param password password the user enterd
     * @return true if the inputs are valid
     */
    public boolean validateInput(String fullName, String username, String password) {
        if (fullName.isEmpty()) {
            view.setFullNameFieldError(R.string.err_et_fullname);
            return false;
        }
        if (username.isEmpty()) {
            view.setFullNameFieldError(R.string.err_et_username);
            return false;
        }
        if (password.isEmpty()) {
            view.setPasswordFieldError(R.string.err_et_password);
            return false;
        }
        return true;
    }


    public void setAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthStateListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
