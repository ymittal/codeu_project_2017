package com.google.codeu.chatme.view.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.SimpleDividerItemDecoration;
import com.google.codeu.chatme.common.view.BaseFragment;
import com.google.codeu.chatme.view.adapter.UserListAdapter;

public class UsersFragment extends BaseFragment {

    /**
     * Required empty public constructor
     */
    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        initializeUI(view);
        return view;
    }

    /**
     * Sets up user interface by loading the list of users
     *
     * @param view inflated {@link UsersFragment} layout view
     */
    private void initializeUI(View view) {
        RecyclerView rvUserList = (RecyclerView) view.findViewById(R.id.userList);
        rvUserList.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListAdapter userListAdapter = new UserListAdapter(getContext());
        rvUserList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        rvUserList.setAdapter(userListAdapter);

        userListAdapter.loadUsers();
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
