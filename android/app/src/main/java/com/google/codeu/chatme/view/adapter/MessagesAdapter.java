package com.google.codeu.chatme.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.model.Conversation;
import com.google.codeu.chatme.model.Message;
import com.google.codeu.chatme.model.PublicUserDetails;
import com.google.codeu.chatme.presenter.MessagesPresenter;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.message.MessagesActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.codeu.chatme.view.adapter.ConversationListAdapter.getDirectRecipientId;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>
        implements MessagesAdapterView {

    private MessagesPresenter presenter;
    private Context context;

    private List<Message> messages = new ArrayList<>();
    private Conversation conversation;
    private HashMap<String, PublicUserDetails> participantDetailsMap;

    public MessagesAdapter(Context context, Conversation conversation) {
        this.presenter = new MessagesPresenter(this);
        this.presenter.postConstruct();

        this.context = context;
        this.conversation = conversation;

        presenter.getParticipantDetails(conversation.getParticipants());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvMessage.setText(message.getContent());

        if (message.getAuthor().equals(FirebaseUtil.getCurrentUserUid())) {
            holder.cvMessage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.msgOutgoing));

            LayoutParams params = (LayoutParams) holder.cvMessage.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.cvMessage.setLayoutParams(params);
        } else {
            holder.cvMessage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.msgIncoming));

            LayoutParams params = (LayoutParams) holder.cvMessage.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.cvMessage.setLayoutParams(params);

            if (participantDetailsMap != null && conversation.getIsGroup()) {
                PublicUserDetails pDetails = participantDetailsMap.get(message.getAuthor());
                holder.tvSender.setVisibility(View.VISIBLE);
                holder.tvSender.setText(pDetails.getFullName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    /**
     * Retrieves all messages from the database for the specific conversation
     *
     * @param conversationId id of conversation to load messages of
     */
    public void loadMessages(String conversationId) {
        presenter.loadMessages(conversationId);
    }

    /**
     * Saves a new message to Firebase database
     *
     * @param newMessage Message object to be added
     */
    public void sendMessage(Message newMessage) {
        presenter.sendMessage(newMessage);
    }

    @Override
    public void setMessagesOnView(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public void setParticipantDetailsMap(HashMap<String, PublicUserDetails> body) {
        this.participantDetailsMap = body;
        setScreenTitle();
        notifyDataSetChanged();
    }

    @Override
    public void setScreenTitle() {
        String title;
        if (conversation.getIsGroup()) {
            title = conversation.getGroupName();
        } else {
            String recipientId = getDirectRecipientId(conversation.getParticipants());
            PublicUserDetails pDetails = participantDetailsMap.get(recipientId);
            title = pDetails.getFullName();
        }
        ((MessagesActivity) context).setTitle(title);
    }

    /**
     * A RecyclerView ViewHolder class to encapsulate various views elements
     * of a message item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSender;
        private TextView tvMessage;
        private CardView cvMessage;

        ViewHolder(View itemView) {
            super(itemView);
            tvSender = (TextView) itemView.findViewById(R.id.tvSender);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            cvMessage = (CardView) itemView.findViewById(R.id.cvMessage);
        }
    }
}
