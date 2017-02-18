package com.example.andrew.vkclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.andrew.vkclient.fragments.FriendsFragment;
import com.example.andrew.vkclient.fragments.FriendsOnlineFragment;

public class FriendsFragmentPagerAdapter extends FragmentPagerAdapter {
    public static final String TAG = "FriendsFragmentPagerAdapter";

    private final int PAGE_COUNT = 2;
    private final String[] tabTitles = new String[] {
            "Friends",
            "Online"
    };

    public FriendsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return FriendsFragment.newInstance();
            case 1: return FriendsOnlineFragment.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
