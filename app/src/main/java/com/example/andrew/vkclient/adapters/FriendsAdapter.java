package com.example.andrew.vkclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andrew.vkclient.R;
import com.example.andrew.vkclient.activity.ProfileActivity;
import com.example.andrew.vkclient.util.Constants;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    public static final String TAG = "FriendsAdapter";

    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private VKUsersArray mFriends;

    private int lastPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mContainer;
        public ImageView mAvatarImageView;
        public TextView mNameTextView;
        public TextView mOnlineTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContainer = (RelativeLayout) itemView.findViewById(R.id.rlFriendContainer);
            mAvatarImageView = (ImageView) itemView.findViewById(R.id.ivFriendAvatar);
            mNameTextView = (TextView) itemView.findViewById(R.id.tvFriendName);
            mOnlineTextView = (TextView) itemView.findViewById(R.id.tvFriendOnline);
        }

        public void clearAnimation() {
            mContainer.clearAnimation();
        }
    }

    public FriendsAdapter(Context context, RecyclerView.LayoutManager layoutManager, VKUsersArray friends) {
        mContext = context;
        mLayoutManager = (LinearLayoutManager) layoutManager;
        mFriends = friends;
        lastPosition = mLayoutManager.findLastVisibleItemPosition();
    }

    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder, int position) {
        final VKApiUserFull user = mFriends.get(position);
        Picasso.with(mContext).load(user.photo_100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.mAvatarImageView);
        holder.mNameTextView.setText(user.toString());

        if(user.online && user.online_mobile) {
            holder.mOnlineTextView.setText(mContext.getResources().getString(R.string.online_mobile));
        } else if(user.online) {
            holder.mOnlineTextView.setText(mContext.getResources().getString(R.string.online));
        } else {
            holder.mOnlineTextView.setText(mContext.getResources().getString(R.string.offline));
        }

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProfileActivity(user.id);
            }
        });

        setAnimation(holder.mContainer, position);
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }

    private void startProfileActivity(int userID) {
        Intent profileIntent = new Intent(mContext, ProfileActivity.class);
        Bundle b = new Bundle();
        b.putInt(Constants.USER_ID, userID);
        profileIntent.putExtras(b);
        mContext.startActivity(profileIntent);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position < lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.setAnimation(animation);
        } else {
            lastPosition = -1;
        }
    }
}
