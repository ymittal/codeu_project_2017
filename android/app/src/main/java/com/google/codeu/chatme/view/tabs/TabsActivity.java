package com.google.codeu.chatme.view.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.codeu.chatme.R;

/**
 * This activity controls the tab panel for switching between the following three fragments
 * - {@link ConversationsFragment}
 * - {@link ProfileFragment}
 * - {@link UsersFragment}
 */
public class TabsActivity extends AppCompatActivity
        implements ConversationsFragment.OnFragmentInteractionListener,
        UsersFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        initViewPager(mPager);

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(mPager);
    }

    public void initViewPager(ViewPager mPager) {
        mPager.setAdapter(new MyViewPagerAdaper(getSupportFragmentManager()));
        mPager.setOffscreenPageLimit(MyViewPagerAdaper.NUM_TABS);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                hideKeyboard();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
    }

    public class MyViewPagerAdaper extends FragmentPagerAdapter {

        private static final int NUM_TABS = 3;

        public MyViewPagerAdaper(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ConversationsFragment();
                case 1:
                    return new ProfileFragment();
                case 2:
                    return new UsersFragment();
                default:
                    return new ConversationsFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_title_chats);
                case 1:
                    return getString(R.string.tab_title_profile);
                case 2:
                    return getString(R.string.tab_title_users);
                default:
                    return getString(R.string.tab_title_chats);
            }
        }
    }
}
