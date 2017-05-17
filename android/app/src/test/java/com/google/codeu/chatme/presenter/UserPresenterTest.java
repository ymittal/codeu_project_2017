package com.google.codeu.chatme.presenter;

import android.util.Log;

import com.google.codeu.chatme.model.User;
import com.google.codeu.chatme.utility.FirebaseUtil;
import com.google.codeu.chatme.view.adapter.UserListAdapterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseUtil.class, FirebaseDatabase.class, Log.class})
public class UserPresenterTest {

    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    @Mock
    private UserListAdapterView view;

    @InjectMocks
    private UserPresenter presenter;

    @Before
    public void setUp() throws Exception {
        mockStatic(FirebaseUtil.class);
        mockStatic(FirebaseDatabase.class);
        mockStatic(Log.class);

        when(FirebaseUtil.getCurrentUserUid()).thenReturn(CURRENT_USER_ID);
    }

    @Test
    public void testLoadUsers() {
        FirebaseDatabase dbInstance = mock(FirebaseDatabase.class, RETURNS_DEEP_STUBS);
        DatabaseReference childRef = mock(DatabaseReference.class);
        when(FirebaseDatabase.getInstance()).thenReturn(dbInstance);
        when(dbInstance.getReference().child("users")).thenReturn(childRef);
        presenter.postConstruct();

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener vel = (ValueEventListener) invocation.getArguments()[0];

                DataSnapshot dataSnapshot = mock(DataSnapshot.class);
                Iterable<DataSnapshot> dsUserList = generateUsersIterable();
                when(dataSnapshot.getChildren()).thenReturn(dsUserList);
                vel.onDataChange(dataSnapshot);

                // verifying result
                List<User> userList = new ArrayList<>();
                for (DataSnapshot user : dsUserList) {
                    if (!user.getKey().equals(FirebaseUtil.getCurrentUserUid())) {
                        userList.add(user.getValue(User.class));
                    }
                }
                verify(view).setUserList(userList);

                return null;
            }
        }).when(childRef).addValueEventListener((ValueEventListener) Matchers.any());

        presenter.loadUsers();
    }

    private Iterable<DataSnapshot> generateUsersIterable() {
        List<DataSnapshot> users = new ArrayList<>();

        DataSnapshot dsUser1 = mock(DataSnapshot.class);
        User user1 = new User();
        user1.setId("id1");
        when(dsUser1.getKey()).thenReturn(user1.getId());
        when(dsUser1.getValue(User.class)).thenReturn(user1);

        DataSnapshot dsCurrentUser = mock(DataSnapshot.class);
        User currentUser = new User();
        currentUser.setId(CURRENT_USER_ID);
        when(dsCurrentUser.getKey()).thenReturn(CURRENT_USER_ID);
        when(dsCurrentUser.getValue(User.class)).thenReturn(currentUser);

        DataSnapshot dsUser2 = mock(DataSnapshot.class);
        User user2 = new User();
        user2.setId("id2");
        when(dsUser2.getKey()).thenReturn(user2.getId());
        when(dsUser2.getValue(User.class)).thenReturn(user2);

        users.add(dsUser1);
        users.add(dsUser2);
        users.add(dsCurrentUser);

        return users;
    }

}