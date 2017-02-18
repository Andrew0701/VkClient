package com.example.andrew.vkclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andrew.vkclient.DividerItemDecoration;
import com.example.andrew.vkclient.R;
import com.example.andrew.vkclient.adapters.FriendsAdapter;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

public class FriendsOnlineFragment extends Fragment {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public FriendsOnlineFragment() {
    }

    public static FriendsOnlineFragment newInstance() {
        FriendsOnlineFragment fragment = new FriendsOnlineFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends_online, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvFriendsOnline);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_friends_online);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showOnlineFriends();
            }
        });
        showOnlineFriends();
    }

    private void showOnlineFriends() {
        VKParameters parameters = VKParameters.from(
                VKApiConst.FIELDS, "photo_50, photo_100, online",
                "order", "hints");
        VKRequest request = VKApi.friends().get(parameters);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKUsersArray friends = (VKUsersArray) response.parsedModel;
                VKUsersArray friendsOnline = getOnlineFriends(friends);

                mAdapter = new FriendsAdapter(getActivity(), mLayoutManager, friendsOnline);
                mRecyclerView.setAdapter(mAdapter);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private VKUsersArray getOnlineFriends(VKUsersArray friends) {
        VKUsersArray friendsOnline = new VKUsersArray();
        for (VKApiUserFull friend : friends) {
            if(friend.online) {
                friendsOnline.add(friend);
            }
        }
        return friendsOnline;
    }

}
