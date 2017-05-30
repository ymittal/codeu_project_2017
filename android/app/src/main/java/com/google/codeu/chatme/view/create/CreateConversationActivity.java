package com.google.codeu.chatme.view.create;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.view.BaseActivity;
import com.google.codeu.chatme.view.tabs.UsersFragment;

public class CreateConversationActivity extends BaseActivity
        implements UsersFragment.OnFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
