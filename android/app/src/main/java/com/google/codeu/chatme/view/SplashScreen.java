package com.google.codeu.chatme.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.view.BaseActivity;
import com.google.codeu.chatme.view.login.LoginActivity;

public class SplashScreen extends BaseActivity {

    public static final int SPLASH_LENGTH_IN_MILLISECONDS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView txtView = (TextView) findViewById(R.id.textView);
        txtView.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this, android.R.anim.slide_in_left));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_LENGTH_IN_MILLISECONDS);
    }
}