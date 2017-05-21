package com.google.codeu.chatme.view.create;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.view.BaseActivity;
import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.presenter.CreateGroupPresenter;
import com.google.codeu.chatme.view.adapter.ConversationListAdapter;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.codeu.chatme.view.message.MessagesActivity;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

public class CreateGroupActivity extends BaseActivity implements
        CreateGroupView, View.OnClickListener {

    private static final int GALLERY_INTENT = 73;

    private CreateGroupPresenter presenter;

    private Button btnStartGroup;
    private EditText etGroupName;
    private CircularImageView ivGroupAvatar;

    private Conversation conversation;
    private Uri picData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // retrieves the to-be created conversation object
        Bundle extras = getIntent().getExtras();
        conversation = (Conversation) extras.getSerializable(UserListAdapter.GROUP_CONV_EXTRA);

        presenter = new CreateGroupPresenter(this);
        presenter.postConstruct();

        setupUI();
    }

    /**
     * Initializes view elements
     */
    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnStartGroup = (Button) findViewById(R.id.btnStartGroup);
        etGroupName = (EditText) findViewById(R.id.etGroupName);
        ivGroupAvatar = (CircularImageView) findViewById(R.id.ivGroupAvatar);

        btnStartGroup.setOnClickListener(this);
        ivGroupAvatar.setOnClickListener(this);

        setGroupAvatar(null);
    }

    /**
     * Sets group avatar on the UI. If null, places the placeholder group avatar instead
     *
     * @param picData Uri containing group avatar data
     */
    private void setGroupAvatar(Uri picData) {
        if (picData != null) {
            Picasso.with(this)
                    .load(picData)
                    .placeholder(R.drawable.placeholder_group)
                    .error(R.drawable.placeholder_group)
                    .fit()
                    .into(ivGroupAvatar);
        } else {
            Picasso.with(this)
                    .load(R.drawable.placeholder_group)
                    .placeholder(R.drawable.placeholder_group)
                    .into(ivGroupAvatar);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // start group button clicked
            case R.id.btnStartGroup:
                conversation.setGroupName(etGroupName.getText().toString());
                presenter.addGroupConversation(conversation, picData);
                break;

            // opens gallery to pick a new group avatar
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
            picData = data.getData();
            setGroupAvatar(picData);
        }
    }

    @Override
    public void openMessageActivity(String conversationId) {
        Bundle bundle = new Bundle();
        conversation.setId(conversationId);
        bundle.putSerializable(ConversationListAdapter.CONV_MESSAGES_EXTRA, conversation);

        Intent mIntent = new Intent(this, MessagesActivity.class);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        finish();
    }
}
