package com.google.codeu.chatme.view.create;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.presenter.CreateGroupPresenter;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.codeu.chatme.view.message.MessagesActivity;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

public class CreateGroupActivity extends AppCompatActivity implements
        CreateGroupView, View.OnClickListener {

    private static final int GALLERY_INTENT = 73;

    private CreateGroupPresenter presenter;

    private ProgressDialog mProgressDialog;

    private Button btnStartGroup;
    private EditText etGroupName;
    private CircularImageView ivGroupAvatar;

    private Conversation conversation;
    private Uri picData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // retrieves the conversation sent from UsersFragment
        Bundle extras = getIntent().getExtras();
        conversation = (Conversation) extras.getSerializable(UserListAdapter.CONV_EXTRA);

        presenter = new CreateGroupPresenter(this);

        initializeUI();
    }

    /**
     * Initializes view elements
     */
    private void initializeUI() {
        btnStartGroup = (Button) findViewById(R.id.btnStartGroup);
        etGroupName = (EditText) findViewById(R.id.etGroupName);
        ivGroupAvatar = (CircularImageView) findViewById(R.id.ivGroupAvatar);

        btnStartGroup.setOnClickListener(this);
        ivGroupAvatar.setOnClickListener(this);

        Picasso.with(this)
                .load(R.drawable.placeholder_person)
                .placeholder(R.drawable.placeholder_person)
                .into(ivGroupAvatar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnStartGroup:
                conversation.setGroupName(etGroupName.getText().toString());
                presenter.addGroupConversation(conversation, picData);
                break;

            // opens gallery to pick a new profile picture
            case R.id.ivGroupAvatar:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            Picasso.with(this)
                    .load(data.getData())
                    .noPlaceholder()
                    .fit()
                    .into(ivGroupAvatar);
            picData = data.getData();
        }
    }

    @Override
    public void openMessageActivity(String conversationId) {
        Intent mIntent = new Intent(this, MessagesActivity.class);
        mIntent.putExtra(UserListAdapter.CONV_ID_EXTRA, conversationId);
        startActivity(mIntent);
    }

    public void showProgressDialog(int messsage) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(messsage));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
