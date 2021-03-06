package com.google.codeu.chatme.view.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.common.view.BaseFragment;
import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.presenter.ProfilePresenter;
import com.google.codeu.chatme.view.login.LoginActivity;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends BaseFragment implements ProfileView, View.OnClickListener {

    private static final int GALLERY_INTENT = 42;

    // fragment view elements
    private EditText etPassword;
    private EditText etUsername;
    private EditText etFullName;
    private Button btnSaveChanges;
    private Button btnLogOut;
    private CircularImageView ivProfilePic;

    private ProfilePresenter presenter;

    /**
     * Required empty public constructor
     */
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etFullName = (EditText) view.findViewById(R.id.etFullName);
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        btnSaveChanges = (Button) view.findViewById(R.id.btnSaveChanges);
        btnLogOut = (Button) view.findViewById(R.id.btnLogOut);
        ivProfilePic = (CircularImageView) view.findViewById(R.id.ivProfilePic);

        btnSaveChanges.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);

        presenter = new ProfilePresenter(this);
        presenter.postConstruct();

        presenter.getUserProfile();
        setProfilePicture(null);    // shows placeholder image
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setAuthStateListener();
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
    public void onStop() {
        super.onStop();
        presenter.removeAuthStateListener();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void setUserProfile(User userData) {
        etUsername.setText(userData.getUsername());
        etFullName.setText(userData.getFullName());
        setProfilePicture(userData.getPhotoUrl());
    }

    @Override
    public void setProfilePicture(String downloadUrl) {
        if (downloadUrl != null && !downloadUrl.isEmpty()) {
            Picasso.with(getContext())
                    .load(downloadUrl)
                    .placeholder(R.drawable.placeholder_person)
                    .error(R.drawable.placeholder_person)
                    .fit()
                    .into(ivProfilePic, new Callback() {
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
            Picasso.with(getContext())
                    .load(R.drawable.placeholder_person)
                    .placeholder(R.drawable.placeholder_person)
                    .into(ivProfilePic);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSaveChanges:
                String fullName = etFullName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                presenter.updateUser(fullName, username, password);
                break;

            case R.id.btnLogOut:
                presenter.signOut();
                break;

            // opens gallery to pick a new profile picture
            case R.id.ivProfilePic:
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
            presenter.uploadProfilePictureToStorage(data.getData());
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
