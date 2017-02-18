package com.example.andrew.vkclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.andrew.vkclient.DividerItemDecoration;
import com.example.andrew.vkclient.R;
import com.example.andrew.vkclient.adapters.WallAdapter;
import com.example.andrew.vkclient.listeners.EndlessRecyclerOnScrollListener;
import com.example.andrew.vkclient.model.VkWallExtended;
import com.example.andrew.vkclient.util.Constants;
import com.example.andrew.vkclient.util.WallExtendedResponseParser;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class WallActivity extends AppCompatActivity {
    public static final String TAG = "WallActivity";

    private RecyclerView mWallRecyclerView;
    private WallAdapter mWallAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mUserID;

    private int numberOfRecords = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        initFields();
        getIntentData();
        getWallItems(0);
    }

    private void initFields() {
        mWallRecyclerView = (RecyclerView) findViewById(R.id.rvWall);
        mWallAdapter = new WallAdapter(this);
        mWallRecyclerView.setAdapter(mWallAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mWallRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mWallRecyclerView.addItemDecoration(itemDecoration);

        mWallRecyclerView.addOnScrollListener(
                new EndlessRecyclerOnScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getWallItems(numberOfRecords *currentPage);
            }
        });
    }

    private void getIntentData() {
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mUserID = b.getInt(Constants.USER_ID);
        }
    }

    private void getWallItems(int offset) {
        VKParameters parameters = VKParameters.from(
                VKApiConst.OWNER_ID, mUserID,
                VKApiConst.COUNT, numberOfRecords,
                VKApiConst.OFFSET, offset,
                VKApiConst.EXTENDED, 1);
        VKRequest request = VKApi.wall().get(parameters);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d(TAG, "getWallItems() onError() " + error.toString());
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, "getWallItems() onComplete() " + response.responseString);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                VkWallExtended wallItems = WallExtendedResponseParser.parse(response.responseString);
                mWallAdapter.updateAdapterData(wallItems);
            }
        });
    }
}
