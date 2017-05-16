package com.google.codeu.chatme.view.create;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.presenter.CreateGroupPresenter;
import com.google.codeu.chatme.view.message.MessagesActivity;


public class CreateGroupActivity  extends AppCompatActivity implements
        CreateGroupView, View.OnClickListener {


    private CreateGroupPresenter presenter;

    private Button btnStartGroup;
    private EditText etGroupName;
    private ImageView ivGroupAvatar;


    public static final String CONV_ID_EXTRA = "CONV_ID_EXTRA";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new CreateGroupPresenter(this);

        btnStartGroup = (Button) this.findViewById(R.id.btnStartGroup);
        etGroupName = (EditText) this.findViewById(R.id.etGroupName);
        ivGroupAvatar = (ImageView) this.findViewById(R.id.ivGroupAvatar);

        btnStartGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void openMessageActivity(String conversationId) {
        Intent mIntent = new Intent(this, MessagesActivity.class);
        mIntent.putExtra(CONV_ID_EXTRA, conversationId);
        startActivity(mIntent);
    }

    @Override
    public void setGroupPicture(String downloadUrl) {

    }
}
