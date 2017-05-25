package com.google.codeu.chatme.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.presenter.CreateConversationPresenter;
import com.google.codeu.chatme.presenter.UserPresenter;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.create.CreateGroupActivity;
import com.google.codeu.chatme.view.message.MessagesActivity;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A RecyclerView Adapter to bind the list of users displayed in UsersFragment
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>
        implements UserListAdapterView {

    public static final String GROUP_CONV_EXTRA = "create group conversation extra";

    private CreateConversationPresenter createConvPresenter;
    private UserPresenter userPresenter;
    private Context context;

    private List<User> users = new ArrayList<>();

    /**
     * Multi-selector to select multiple user list items
     */
    private MultiSelector mMultiSelector = new MultiSelector();

    public UserListAdapter(Context context) {
        userPresenter = new UserPresenter(this);
        userPresenter.postConstruct();
        createConvPresenter = new CreateConversationPresenter(this);
        createConvPresenter.postConstruct();

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
        holder.setUserId(user.getId());

        holder.setHolderPicture(user.getPhotoUrl());
        if (user.getFullName() != null) {
            holder.tvName.setText(user.getFullName());
        } else {
            holder.tvName.setText(user.getUsername());
        }

        if (user.getIsOnline()) {
            holder.tvLastSeen.setText(R.string.active_status_online);
        } else {
            holder.tvLastSeen.setText(user.getReadableLastSeen(context));
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
    public void openMessageActivity(Conversation conversation) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConversationListAdapter.CONV_MESSAGES_EXTRA, conversation);

        Intent mIntent = new Intent(context, MessagesActivity.class);
        mIntent.putExtras(bundle);
        context.startActivity(mIntent);
    }

    @Override
    public void openCreateGroupActivity(Conversation conversation) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GROUP_CONV_EXTRA, conversation);

        Intent mIntent = new Intent(context, CreateGroupActivity.class);
        mIntent.putExtras(bundle);
        context.startActivity(mIntent);
    }

    /**
     * A RecyclerView ViewHolder class to encapsulate various views elements
     * of a user list item
     */
    class ViewHolder extends SwappingHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private TextView tvName;
        private TextView tvLastSeen;
        private CircularImageView civUserPic;

        private String userId;

        ViewHolder(View itemView) {
            super(itemView, mMultiSelector);
            setSelectionModeBackgroundDrawable(getSelectedBackground());
            setSelectionModeStateListAnimator(null);

            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvLastSeen = (TextView) itemView.findViewById(R.id.tvLastSeen);
            civUserPic = (CircularImageView) itemView.findViewById(R.id.civUserPic);
        }

        private StateListDrawable getSelectedBackground() {
            ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(context,
                    R.color.user_list_item_selected));
            StateListDrawable sld = new StateListDrawable();
            sld.addState(new int[]{android.R.attr.state_activated}, cd);
            sld.addState(StateSet.WILD_CARD, null);
            return sld;
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
            if (!mMultiSelector.tapSelection(this)) {
                // creates a new conversation object and add recipient as a participant
                Conversation conv = new Conversation(FirebaseUtil.getCurrentUserUid());
                conv.addParticipant(userId);
                conv.setIsGroup(false);

                // checks for conversation duplicates and adds a conversation only if unique
                createConvPresenter.openConversationMessages(conv);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (!mMultiSelector.isSelectable()) {
                ((AppCompatActivity) context).startSupportActionMode(selectorCallback);
                mMultiSelector.setSelectable(true);
                mMultiSelector.setSelected(ViewHolder.this, true);
                return true;
            }
            return false;
        }
    }

    /**
     * Callback to handle clicks on contextual action bar which pops up when a user list item
     * is long tapped on
     */
    private ModalMultiSelectorCallback selectorCallback = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            super.onCreateActionMode(mode, menu);
            mode.getMenuInflater().inflate(R.menu.user_list_item_context, menu);
            return true;
        }

        /**
         * Launches CreateGroupActivity if create group item was clicked and at least
         * one user was selected
         *
         * @param mode action mode
         * @param menuItem menu item clicked
         * @return true if click is handled properly
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.menu_item_create_group) {
                mode.finish();

                Conversation conv = new Conversation(FirebaseUtil.getCurrentUserUid());
                conv.setIsGroup(true);
                for (int i = users.size() - 1; i >= 0; i--) {
                    if (mMultiSelector.isSelected(i, 0)) {
                        conv.addParticipant(users.get(i).getId());
                    }
                }
                if (conv.getParticipants().size() >= Conversation.MIN_CONV_PARTICIPANTS) {
                    openCreateGroupActivity(conv);
                } else {
                    Toast.makeText(context, context.getString(R.string.min_conv_participants),
                            Toast.LENGTH_SHORT).show();
                }

                return true;
            }
            return false;
        }
    };
}