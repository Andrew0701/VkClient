package com.example.andrew.vkclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrew.vkclient.R;
import com.example.andrew.vkclient.util.Constants;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ProfileActivity.class.getName();

    private ImageView mAvatarImageView;
    private TextView mNameTextView;
    private TextView mOnlineTextView;
    private Button mMessageButton;
    private Button mWallButton;

    private VKApiUserFull mUserProfile;
    private int mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initFields();
        getIntentData();
        if (mUserProfile != null) {
            setFields();
        } else {
            getUserProfileData(mUserID);
        }
    }

    private void initFields() {
        mAvatarImageView = (ImageView) findViewById(R.id.ivProfilePhoto);
        mNameTextView = (TextView) findViewById(R.id.tvProfileName);
        mOnlineTextView = (TextView) findViewById(R.id.tvProfileOnline);
        mMessageButton = (Button) findViewById(R.id.btnProfileMessage);
        mWallButton = (Button) findViewById(R.id.btnProfileWall);
        mWallButton.setOnClickListener(this);
    }

    private void getIntentData() {
        Log.d(TAG, "getIntentData()");
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mUserProfile = b.getParcelable(Constants.PROFILE);
            mUserID = b.getInt(Constants.USER_ID);
        }
    }

    private void getUserProfileData(final int userId) {
        Log.d(TAG, "getUserProfileData() id " + userId);
        VKParameters parameters = VKParameters.from(
                VKApiConst.USER_IDS, userId,
                VKApiConst.FIELDS, "photo_50, photo_100, online");
        VKRequest request = VKApi.users().get(parameters);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                mUserProfile = users.get(0);
                if (mUserProfile != null) {
                    setFields();
                }
            }
        });
    }

    private void setFields() {
        Picasso.with(this).load(mUserProfile.photo_100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(mAvatarImageView);
        mNameTextView.setText(mUserProfile.toString());
        if (mUserProfile.online) {
            mOnlineTextView.setText(getResources().getString(R.string.online));
        } else {
            mOnlineTextView.setText(getResources().getString(R.string.offline));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProfileWall:
                startWallActivity();
                break;
        }
    }

    private void startWallActivity() {
        Intent wallIntent = new Intent(ProfileActivity.this, WallActivity.class);
        Bundle b = new Bundle();
        b.putInt(Constants.USER_ID, mUserID);
        wallIntent.putExtras(b);
        startActivity(wallIntent);
    }
}
