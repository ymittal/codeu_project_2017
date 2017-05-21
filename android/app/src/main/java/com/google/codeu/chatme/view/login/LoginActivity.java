package com.google.codeu.chatme.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.view.BaseActivity;
import com.google.codeu.chatme.presenter.LoginActivityPresenter;
import com.google.codeu.chatme.view.tabs.TabsActivity;

/**
 * @see LoginView for documentation of interface methods
 */
public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private LoginActivityPresenter presenter;

    private Button btnLogin;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnCreateAcnt;

    /**
     * Sets up Firebase Auth to respond to a change in user's sign-in state, initializes view
     * elements
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginActivityPresenter(this);
        presenter.postConstruct();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCreateAcnt = (Button) findViewById(R.id.btnCreateAcnt);
        btnLogin.setOnClickListener(this);
        btnCreateAcnt.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setAuthStateListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.removeAuthStateListener();
    }

    @Override
    public void openTabsActivity() {
        Intent mIntent = new Intent(LoginActivity.this, TabsActivity.class);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void setEmailFieldError(int err_et_email) {
        etEmail.setError(getString(err_et_email));
    }

    @Override
    public void setPasswordFieldError(int err_et_password) {
        etPassword.setError(getString(err_et_password));
    }

    @Override
    public void onClick(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        switch (view.getId()) {

            // login button clicked
            case R.id.btnLogin:
                presenter.signIn(email, password);
                break;

            // create account button clicked
            case R.id.btnCreateAcnt:
                presenter.signUp(email, password);
                break;
        }
    }
}
