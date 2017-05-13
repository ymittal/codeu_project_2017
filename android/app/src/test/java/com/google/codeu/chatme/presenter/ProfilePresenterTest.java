package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.R;
import com.google.codeu.chatme.TestUtility.AutoCompleteTask;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.tabs.ProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseUtil.class, Log.class, FirebaseDatabase.class})
public class ProfilePresenterTest {

    public static final String PASSWORD = "password";

    @Mock
    private FirebaseDatabase firebaseDatabase;
    @Mock
    private FirebaseAuth firebaseAuth;
    @Mock
    private ProfileView view;

    @InjectMocks
    private ProfilePresenter presenter;

    @Before
    public void setUp() throws Exception {
        mockStatic(FirebaseUtil.class);
        mockStatic(Log.class);
        mockStatic(FirebaseDatabase.class);
    }

    @Test
    public void testUpdatePassword() {
        FirebaseUser user = mock(FirebaseUser.class);
        when(FirebaseUtil.getCurrentUser()).thenReturn(user);
        when(user.updatePassword(PASSWORD))
                .thenReturn(new AutoCompleteTask<Void>(null, true, null));

        presenter.updatePassword(PASSWORD);
        verify(view).showProgressDialog(R.string.progress_update_pwd);
        verify(view).hideProgressDialog();
        verify(view).makeToast(R.string.toast_pwd_name);
    }

    @Test
    public void testUpdateEmptyPassword() {
        String passwordEmptyMessage = "empty";

        FirebaseUser user = mock(FirebaseUser.class);
        when(FirebaseUtil.getCurrentUser()).thenReturn(user);
        when(user.updatePassword(""))
                .thenReturn(new AutoCompleteTask<Void>(null, true, new Exception(passwordEmptyMessage)));

        presenter.updatePassword("");
        verify(view, never()).showProgressDialog(R.string.progress_update_pwd);
    }

    @Test
    public void testUpdateWeakPassword() {
        String passwordWeakMessage = "passwordWeakMessage";
        String weakPassword = "123";

        FirebaseUser user = mock(FirebaseUser.class);
        when(FirebaseUtil.getCurrentUser()).thenReturn(user);
        when(user.updatePassword(weakPassword))
                .thenReturn(new AutoCompleteTask<Void>(null, true, new Exception(passwordWeakMessage)));

        presenter.updatePassword(weakPassword);
        verify(view).showProgressDialog(R.string.progress_update_pwd);
        verify(view).hideProgressDialog();
        verify(view).makeToast(passwordWeakMessage);
    }
}