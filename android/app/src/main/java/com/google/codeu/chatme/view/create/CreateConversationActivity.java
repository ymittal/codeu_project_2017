package com.google.codeu.chatme.view.create;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.view.BaseActivity;
import com.google.codeu.chatme.view.adapter.UserListAdapter;

public class CreateConversationActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvUserList = (RecyclerView) this.findViewById(R.id.userList);
        rvUserList.setLayoutManager(new LinearLayoutManager(this));

        UserListAdapter userListAdapter = new UserListAdapter(this);
        rvUserList.setAdapter(userListAdapter);

        userListAdapter.loadUsers();
    }
}
