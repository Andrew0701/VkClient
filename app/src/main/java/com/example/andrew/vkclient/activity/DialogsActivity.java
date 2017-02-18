package com.example.andrew.vkclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.andrew.vkclient.DividerItemDecoration;
import com.example.andrew.vkclient.R;
import com.example.andrew.vkclient.adapters.DialogsAdapter;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

public class DialogsActivity extends AppCompatActivity {
    public static final String TAG = "DialogsActivity";

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mDialogsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);

        mDialogsRecyclerView = (RecyclerView) findViewById(R.id.rvDialogs);
        mLayoutManager = new LinearLayoutManager(this);
        mDialogsRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mDialogsRecyclerView.addItemDecoration(itemDecoration);
        getLongPollServer();
        showDialogs();
    }

    private void getLongPollServer() {
        VKRequest request = new VKRequest("messages.getLongPollServer");
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, "onComplete() response " + response.responseString);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d(TAG, "onError() response " + error.toString());
            }
        });
    }

    private void showDialogs() {
        VKRequest requestGetDialogs = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, "15"));
        requestGetDialogs.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                final VKList<VKApiDialog> dialogs = getDialogsFromResponse(response);
                VKRequest requestGetUsersInfo =
                        VKApi.users().get(VKParameters.from(
                                VKApiConst.USER_IDS, getStringWithUsersID(dialogs),
                                VKApiConst.FIELDS, "photo_100"));
                requestGetUsersInfo.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList<VKApiUserFull> users = getUsersFromResponse(response);
                        mAdapter = new DialogsAdapter(dialogs, users);
                        mDialogsRecyclerView.setAdapter(mAdapter);
                    }
                });

            }
        });
    }

    private VKList<VKApiDialog> getDialogsFromResponse(VKResponse response) {
        VKApiGetDialogResponse dialogResponse = (VKApiGetDialogResponse) response.parsedModel;
        VKList<VKApiDialog> dialogs = dialogResponse.items;
        return dialogs;
    }

    private String getStringWithUsersID(VKList<VKApiDialog> dialogs) {
        String usersID = "";
        for(VKApiDialog dialog : dialogs) {
            usersID += String.valueOf(dialog.message.user_id) + ", ";
        }
        return usersID;
    }

    private VKList<VKApiUserFull> getUsersFromResponse(VKResponse response) {
        return (VKList) response.parsedModel;
    }
}
