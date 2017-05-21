package com.google.codeu.chatme.presenter;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

/**
 * This interface provides functions which primarily relate to login
 * and sign up functionality of this Firebase-powered chat application
 */
interface ProfileInteractor {

    /**
     * Updates current user's profile based on provided parameters
     *
     * @param fullName user's full name
     * @param username user's username
     * @param password user's password
     */
    void updateUser(String fullName, String username, String password);

    /**
     * Logs current user out
     */
    void signOut();

    /**
     * Delete current user's account from firebase auth and database
     */
    void deleteAccount();

    /**
     * Get current user's profile information and store in User object
     */
    void getUserProfile();

    /**
     * Uploads profile picture to Firebase Storage and then updates Database
     * with the picture download url
     *
     * @param data {@link Uri} containing new profile picture
     */
    void uploadProfilePictureToStorage(Uri data);

    /**
     * Adds Auth Listener to {@link FirebaseAuth}
     */
    void setAuthStateListener();

    /**
     * Removes Auth Listener from {@link FirebaseAuth}
     */
    void removeAuthStateListener();
}
