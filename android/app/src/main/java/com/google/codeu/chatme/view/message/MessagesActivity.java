package com.google.codeu.chatme.view.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.Message;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.adapter.ConversationListAdapter;
import com.google.codeu.chatme.view.adapter.MessagesAdapter;

/**
 *
 */
public class MessagesActivity extends AppCompatActivity
        implements View.OnClickListener {

    private EditText etTypeMessage;
    private ImageView btnSend;
    private RecyclerView rvMessageList;

    private MessagesAdapter messagesAdapter;

    private Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // sets activity background (does not resize when keyboard opens)
        getWindow().setBackgroundDrawableResource(R.drawable.messages_bg);

        Bundle bundle = getIntent().getExtras();
        conversation = (Conversation) bundle.getSerializable(ConversationListAdapter.CONV_MESSAGES_EXTRA);

        setupUI();
    }

    /**
     * Initializes UI elements and sets up listeners
     */
    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSend = (ImageView) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnSend.setEnabled(false);

        etTypeMessage = (EditText) findViewById(R.id.etTypeMessage);
        etTypeMessage.addTextChangedListener(messageTextWatcher);

        rvMessageList = (RecyclerView) findViewById(R.id.rvMessageList);
        messagesAdapter = new MessagesAdapter(getApplicationContext(), conversation);

        rvMessageList.setLayoutManager(new LinearLayoutManager(this));
        rvMessageList.setAdapter(messagesAdapter);

        messagesAdapter.loadConversations(conversation.getId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                messagesAdapter.sendMessage(createMessageObject());
                etTypeMessage.setText("");
                break;
        }
    }

    /**
     * Creates a {@link Message} object from the content inside etTypeMessage
     *
     * @return message object to add to database
     */
    public Message createMessageObject() {
        String author = FirebaseUtil.getCurrentUserUid();
        String conversationId = conversation.getId();
        String content = etTypeMessage.getText().toString();

        Message newMessage = new Message(author, content, conversationId);
        return newMessage;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        String currentUser = FirebaseUtil.getCurrentUserUid();
        conversation.setCurrentlyViewing(currentUser, false);
    }

    /**
     * An instance of TextWatcher to detect changes on etTypeMessage
     */
    private TextWatcher messageTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().trim().length() > 0) {
                btnSend.setColorFilter(getColor(R.color.ic_send_enabled));
                btnSend.setEnabled(true);
            } else {
                btnSend.setColorFilter(getColor(R.color.ic_send_disabled));
                btnSend.setEnabled(false);
            }
        }
    };
}
