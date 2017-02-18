package com.example.andrew.vkclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.andrew.vkclient.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.util.VKUtil;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private String[] scope = new String[] {
            VKScope.MESSAGES,
            VKScope.FRIENDS,
            VKScope.STATUS,
            VKScope.WALL,
            VKScope.PHOTOS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        checkAuthorization();
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(fingerprints[0]);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                registerDevice();
                showMenu();
            }

            @Override
            public void onError(VKError error) {
                Log.d(TAG, "Error login " + error.toString());
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkAuthorization() {
        if (VKSdk.wakeUpSession(this)) {
            showMenu();
        } else {
            VKSdk.login(this, scope);
        }
    }

    private void registerDevice() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        String uniqueID = UUID.randomUUID().toString();
        VKParameters parameters = VKParameters.from("token", fcmToken, "device_model", "android",
                "device_id", uniqueID,
                "settings","{\"chat\":\"on\",\"msg\":\"on\", \"friend\":\"on\", \"reply\":\"on\", " +
                        "\"mention\":\"fr_of_fr\"}");
        VKRequest request = new VKRequest("account.registerDevice", parameters);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, "registerDevice() onComplete() " + response.responseString);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d(TAG, "registerDevice() onError() " + error.toString());
            }
        });
    }

    private void showMenu() {
        ListView menuItemsListView = (ListView) findViewById(R.id.lvMenuItems);

        menuItemsListView.setAdapter(new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.vk_navigation)));
        menuItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: 
                        startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, DialogsActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, WallActivity.class));
                        break;
                    case 3:
                        VKSdk.logout();
                        break;
                }
            }
        });
    }
}
