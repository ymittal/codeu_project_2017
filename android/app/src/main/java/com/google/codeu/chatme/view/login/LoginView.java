package com.google.codeu.chatme.view.login;

import com.google.codeu.chatme.common.view.BaseActivityView;

public interface LoginView extends BaseActivityView {

    /**
     * Launches TabsActivity, usually on successful sign up or sign in
     */
    void openTabsActivity();

    /**
     * Displays an error for {@link LoginActivity#etEmail} field
     *
     * @param err_et_email resource id of email field error message
     */
    void setEmailFieldError(int err_et_email);

    /**
     * Displays an error for {@link LoginActivity#etPassword} field
     *
     * @param err_et_password resource id of password field error message
     */
    void setPasswordFieldError(int err_et_password);
}
