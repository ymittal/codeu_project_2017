package com.google.codeu.chatme.view.create;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.presenter.CreateGroupPresenter;
import com.google.codeu.chatme.view.adapter.UserListAdapter;
import com.google.codeu.chatme.view.message.MessagesActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class CreateGroupActivity extends AppCompatActivity implements
        CreateGroupView, View.OnClickListener {

    private static final int GALLERY_INTENT = 42;
    private String conversationId;
    private CreateGroupPresenter presenter;

    private ProgressDialog mProgressDialog;

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

        conversationId = getIntent().getStringExtra(UserListAdapter.CONV_ID_EXTRA);

        presenter = new CreateGroupPresenter(this);

        btnStartGroup = (Button) this.findViewById(R.id.btnStartGroup);
        etGroupName = (EditText) this.findViewById(R.id.etGroupName);
        ivGroupAvatar = (ImageView) this.findViewById(R.id.ivGroupAvatar);

        btnStartGroup.setOnClickListener(this);
        ivGroupAvatar.setOnClickListener(this);

        setGroupPicture(null);    // shows placeholder image
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnStartGroup:
                String groupName = etGroupName.getText().toString();
                presenter.setGroupName(groupName, conversationId);
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
            presenter.uploadGroupPictureToStorage(data.getData(), conversationId);
        }
    }

    @Override
    public void openMessageActivity(String conversationId) {
        Intent mIntent = new Intent(this, MessagesActivity.class);
        mIntent.putExtra(CONV_ID_EXTRA, conversationId);
        startActivity(mIntent);
    }


    public void setGroupPicture(String downloadUrl) {
        if (downloadUrl != null && !downloadUrl.isEmpty()) {
            Picasso.with(this)
                    .load(downloadUrl)
                    .placeholder(R.drawable.default_group_avatar)
                    .error(R.drawable.default_group_avatar)
                    .fit()
                    .into(ivGroupAvatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            hideProgressDialog();
                        }

                        @Override
                        public void onError() {
                            hideProgressDialog();
                        }
                    });
        } else {
            Picasso.with(this)
                    .load(R.drawable.default_group_avatar)
                    .placeholder(R.drawable.default_group_avatar)
                    .into(ivGroupAvatar);
        }
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
