package com.google.codeu.chatme.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.presenter.CreateConversationPresenter;
import com.google.codeu.chatme.presenter.UserPresenter;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.message.MessagesActivity;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} to bind the list of users
 * to the recyclerview in {@link com.google.codeu.chatme.view.tabs.UsersFragment}
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>
        implements UserListAdapterView {

    public static final String CONV_ID_EXTRA = "CONV_ID_EXTRA";

    private List<User> users = new ArrayList<>();

    private final Context context;

    private CreateConversationPresenter createConversationPresenter;
    private UserPresenter userPresenter;

    public UserListAdapter(Context context) {
        this.userPresenter = new UserPresenter(this);
        this.userPresenter.postConstruct();
        this.createConversationPresenter = new CreateConversationPresenter(this);
        this.context = context;
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(v);
    }

    public void loadUsers() {
        this.userPresenter.loadUsers();
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.setUserId(user.getId());

        holder.setHolderPicture(user.getPhotoUrl());
        if (user.getFullName() != null) {
            holder.tvName.setText(user.getFullName());
        } else {
            holder.tvName.setText(user.getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public void setUserList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public void openMessageActivity(String conversationId) {
        Intent mIntent = new Intent(context, MessagesActivity.class);
        mIntent.putExtra(CONV_ID_EXTRA, conversationId);
        context.startActivity(mIntent);
    }

    /**
     * A {@link android.support.v7.widget.RecyclerView.ViewHolder} class to encapsulate
     * various views of a user list item
     */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView tvName;
        private CircularImageView civUserPic;
        private String userId;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            civUserPic = (CircularImageView) itemView.findViewById(R.id.civUserPic);
        }

        private void setUserId(String uid) {
            this.userId = uid;
        }

        private void setHolderPicture(String picUrl) {
            if (picUrl != null && !picUrl.isEmpty()) {
                Picasso.with(context)
                        .load(picUrl)
                        .placeholder(R.drawable.placeholder_person)
                        .error(R.drawable.placeholder_person)
                        .fit()
                        .into(civUserPic);
            } else {
                Picasso.with(context)
                        .load(R.drawable.placeholder_person)
                        .placeholder(R.drawable.placeholder_person)
                        .into(this.civUserPic);
            }
        }

        @Override
        public void onClick(View view) {
            // Create new conversation object, set Owner and add participant
            Conversation conversation = new Conversation(FirebaseUtil.getCurrentUser().getUid());
            conversation.addParticipant(userId);

            // Attempt to add conversation object to Firebase DB and trigger Messages Activity
            createConversationPresenter.addConversation(conversation);
        }
    }
}