package com.google.codeu.chatme.presenter;

import com.google.firebase.auth.FirebaseAuth;

/**
 * This interface provides functions which primarily relate to login
 * and sign up functionality of this Firebase-powered chat application
 */
interface LoginActivityInteractor {

    /**
     * Attempts to create an account with given account credentials
     *
     * @param email    user email
     * @param password user password
     */
    void signUp(String email, String password);

    /**
     * Attempts to log user in with given credentials
     *
     * @param email    user email
     * @param password user password
     */
    void signIn(String email, String password);

    /**
     * Adds Auth Listener to {@link FirebaseAuth}
     */
    void setAuthStateListener();

    /**
     * Removes Auth Listener from {@link FirebaseAuth}
     */
    void removeAuthStateListener();
}
