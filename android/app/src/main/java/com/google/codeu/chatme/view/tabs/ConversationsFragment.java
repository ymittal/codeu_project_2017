package com.google.codeu.chatme.view.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.view.BaseFragment;
import com.google.codeu.chatme.view.adapter.ConversationListAdapter;
import com.google.codeu.chatme.view.create.CreateConversationActivity;

public class ConversationsFragment extends BaseFragment implements ConversationsView, View.OnClickListener {

    /**
     * Required empty public constructor
     */
    public ConversationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        initializeUI(view);
        return view;
    }

    /**
     * Loads the list of conversations for the current user in the recyclerview
     *
     * @param view inflated layout view
     */
    private void initializeUI(View view) {
        ImageButton btnCreateConv = (ImageButton) view.findViewById(R.id.btnCreateConversation);
        btnCreateConv.setOnClickListener(this);

        RecyclerView rvChatList = (RecyclerView) view.findViewById(R.id.rvChatList);
        rvChatList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ConversationListAdapter convListAdapter = new ConversationListAdapter(getContext());
        rvChatList.setAdapter(convListAdapter);

        convListAdapter.loadConversations();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // create conversation button clicked
            case R.id.btnCreateConversation:
                openCreateConversationActivity();
                break;
        }
    }

    @Override
    public void openCreateConversationActivity() {
        Intent mIntent = new Intent(getActivity(), CreateConversationActivity.class);
        getActivity().startActivity(mIntent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
