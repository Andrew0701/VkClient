package com.example.andrew.vkclient.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.vk.sdk.api.model.VKUsersArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mFriendsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FriendsFragment() {
    }

    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFriendsRecyclerView = (RecyclerView) view.findViewById(R.id.rvFriends);
        mLayoutManager = new LinearLayoutManager(getContext());
        mFriendsRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        mFriendsRecyclerView.addItemDecoration(itemDecoration);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_friends);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showFriends();
            }
        });

        showFriends();
    }

    private void showFriends() {
        VKParameters parameters = VKParameters.from(
                VKApiConst.FIELDS, "photo_50, photo_100, online",
                "order", "hints");
        VKRequest request = VKApi.friends().get(parameters);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), getResources().getString(R.string.error_get_data),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKUsersArray friends = (VKUsersArray) response.parsedModel;
                mAdapter = new FriendsAdapter(getActivity(), mLayoutManager, friends);
                mFriendsRecyclerView.setAdapter(mAdapter);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
