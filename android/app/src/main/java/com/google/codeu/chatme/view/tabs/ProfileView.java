package com.google.codeu.chatme.view.tabs;

import com.google.codeu.chatme.common.view.BaseFragmentView;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.view.login.LoginActivity;

public interface ProfileView extends BaseFragmentView {

    /**
     * Launches {@link LoginActivity}, usually on successful sign out
     */
    void openLoginActivity();

    /**
     * Sets user profile data, including their full name and username
     *
     * @param userData user object containing data retrieved from Firebase
     */
    void setUserProfile(User userData);

    /**
     * Sets user's profile picture from its download url
     *
     * @param downloadUrl url of profile picture
     */
    void setProfilePicture(String downloadUrl);
}
