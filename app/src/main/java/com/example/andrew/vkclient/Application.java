package com.example.andrew.vkclient;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.andrew.vkclient.activity.MainActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

public class Application extends android.app.Application {

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken, @Nullable VKAccessToken newToken) {
            if (newToken == null) {
                Intent intent = new Intent(Application.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.customInitialize(this, getResources().getInteger(R.integer.com_vk_sdk_AppId), "5.60");
    }
}
