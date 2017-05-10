package com.google.codeu.chatme.view.create;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import com.google.codeu.chatme.R;
import com.google.codeu.chatme.view.adapter.UserListAdapter;

public class CreateConversationActivity extends AppCompatActivity
        implements CreateConversationView {

    private RecyclerView rvUserList;
    private UserListAdapter userListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation);
        rvUserList = (RecyclerView) this.findViewById(R.id.userList);
        rvUserList.setLayoutManager(new LinearLayoutManager(this));
        userListAdapter = new UserListAdapter(this);
        rvUserList.setAdapter(userListAdapter);
        userListAdapter.loadUsers();
    }
}
