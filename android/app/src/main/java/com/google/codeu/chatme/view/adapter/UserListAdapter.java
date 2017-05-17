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

    private CreateConversationPresenter createConvPresenter;
    private UserPresenter userPresenter;

    public UserListAdapter(Context context) {
        this.userPresenter = new UserPresenter(this);
        this.userPresenter.postConstruct();
        this.createConversationPresenter = new CreateConversationPresenter(this);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(v);
    }

    public void loadUsers() {
        this.userPresenter.loadUsers();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = users.get(position);

        holder.setHolderPicture(user.getPhotoUrl());
        if (user.getFullName() != null) {
            holder.tvName.setText(user.getFullName());
        } else {
            holder.tvName.setText(user.getUsername());
        }

        if (user.isOnline()) {
            holder.tvLastSeen.setText("Online");
        } else {
            holder.tvLastSeen.setText(user.getReadableLastSeen(context));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creates a new conversation object and add recipient as a participant
                Conversation conv = new Conversation(FirebaseUtil.getCurrentUserUid());
                conv.addParticipant(user.getId());

                // checks for conversation duplicates and adds a conversation only if unique
                createConvPresenter.openConversationMessages(conv);
            }
        });
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvLastSeen;
        private CircularImageView civUserPic;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvLastSeen = (TextView) itemView.findViewById(R.id.tvLastSeen);
            civUserPic = (CircularImageView) itemView.findViewById(R.id.civUserPic);
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
    }
}