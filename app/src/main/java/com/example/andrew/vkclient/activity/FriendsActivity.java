package com.example.andrew.vkclient.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.andrew.vkclient.R;
import com.example.andrew.vkclient.adapters.FriendsFragmentPagerAdapter;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setupToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpFriends);
        viewPager.setAdapter(new FriendsFragmentPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tlSlidingTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.activity_friends));
        }
    }
}
