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
import com.google.codeu.chatme.model.PublicUserDetails;
import com.google.codeu.chatme.presenter.ConversationsPresenter;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.message.MessagesActivity;
import com.google.codeu.chatme.view.tabs.ConversationsFragment;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} to bind the list of conversations
 * data to the recyclerview in {@link ConversationsFragment}
 *
 * @see ConversationListAdapterView for documentation on interface methods
 */
public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder>
        implements ConversationListAdapterView {

    public static final String CONV_ID_EXTRA = "CONV_ID_EXTRA";
    private final Context context;

    /**
     * List of conversations to display in the list
     */
    private List<Conversation> conversations = new ArrayList<>();

    private ConversationsPresenter presenter;

    /**
     * A map from different conversation participants to their details such as full names and
     * profile picture download urls
     */
    private HashMap<String, PublicUserDetails> participantDetailsMap = new HashMap<>();

    public ConversationListAdapter(Context context) {
        this.presenter = new ConversationsPresenter(this);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Conversation conversation = conversations.get(position);

        holder.tvTimeSent.setText(conversation.getReadableLastMessageTime());

        if (participantDetailsMap != null && participantDetailsMap.size() > 0) {
            if (!conversation.getIsGroup()) {
                String participantId = getDirectRecipientId(conversation.getParticipants());
                PublicUserDetails pDetails = participantDetailsMap.get(participantId);

                holder.tvSender.setText(pDetails.getFullName());
                holder.setHolderPicture(pDetails.getPhotoUrl(), Boolean.FALSE);
                holder.tvLastMessage.setText(conversation.getLastMessageContent());
            } else {
                String lastSenderId = conversation.getLastMessage().getAuthor();
                String lastSenderName = participantDetailsMap.get(lastSenderId).getFullName();

                holder.tvSender.setText(conversation.getGroupName());
                holder.setHolderPicture(conversation.getPhotoUrl(), Boolean.TRUE);
                holder.tvLastMessage.setText(lastSenderName + ": "
                        + conversation.getLastMessageContent());
            }
        } else {
            // http getDetailsFromIds has not completed yet
            holder.setHolderPicture(null, conversation.getIsGroup());
            if (!conversation.getIsGroup()) {
                holder.tvLastMessage.setText(conversation.getLastMessageContent());
            } else {
                holder.tvSender.setText(conversation.getGroupName());
                holder.setHolderPicture(conversation.getPhotoUrl(), Boolean.TRUE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessagesActivity(view.getContext(), conversation.getId());
            }
        });
    }


    /**
     * @param participants list of participant Ids for a particular conversation
     * @return one-on-one recipient id (in current user's scope)
     */
    private String getDirectRecipientId(List<String> participants) {
        for (String id : participants) {
            if (!id.equals(FirebaseUtil.getCurrentUserUid())) {
                return id;
            }
        }
        return null;    // should not be returned ideally
    }

    /**
     * Launches {@link MessagesActivity} for the specific conversation
     *
     * @param context        context to create a startActivity intent
     * @param conversationId id of conversation to display messages of
     */
    private void openMessagesActivity(Context context, String conversationId) {
        Intent mIntent = new Intent(context, MessagesActivity.class);
        mIntent.putExtra(CONV_ID_EXTRA, conversationId);
        context.startActivity(mIntent);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    /**
     * Loads conversations of the current user from Firebase database
     */
    public void loadConversations() {
        this.presenter.loadConversations();
    }

    @Override
    public void setChatList(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    @Override
    public void setParticipantDetailsMap(HashMap<String, PublicUserDetails>
                                                 participantDetailsMap) {
        this.participantDetailsMap = participantDetailsMap;
        notifyDataSetChanged();
    }

    /**
     * A {@link android.support.v7.widget.RecyclerView.ViewHolder} class to encapsulate
     * various views of a conversation list item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSender;
        private TextView tvLastMessage;
        private TextView tvTimeSent;
        private CircularImageView civPic;

        public ViewHolder(View itemView) {
            super(itemView);

            tvSender = (TextView) itemView.findViewById(R.id.tvSender);
            tvLastMessage = (TextView) itemView.findViewById(R.id.tvLastMessage);
            tvTimeSent = (TextView) itemView.findViewById(R.id.tvTimeSent);
            civPic = (CircularImageView) itemView.findViewById(R.id.civPic);
        }

        private void setHolderPicture(String picUrl, boolean isGroup) {
            int placeholderImage;
            if (isGroup) {
                placeholderImage = R.drawable.placeholder_group;
            } else {
                placeholderImage = R.drawable.placeholder_person;
            }

            if (picUrl != null && !picUrl.isEmpty()) {
                Picasso.with(context)
                        .load(picUrl)
                        .placeholder(placeholderImage)
                        .error(placeholderImage)
                        .fit()
                        .into(this.civPic);
            } else {
                Picasso.with(context)
                        .load(placeholderImage)
                        .placeholder(placeholderImage)
                        .into(this.civPic);
            }
        }
    }
}
