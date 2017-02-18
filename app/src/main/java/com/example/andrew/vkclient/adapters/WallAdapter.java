package com.example.andrew.vkclient.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andrew.vkclient.R;
import com.example.andrew.vkclient.model.VkWallExtended;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKAttachments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.ViewHolder> {
    public static final String TAG = "WallAdapter";

    private Context mContext;
    private VkWallExtended mWallItems;

    private int lastPosition = -1;

    public WallAdapter(Context context) {
        mContext = context;
        mWallItems = new VkWallExtended();
    }

    public WallAdapter(VkWallExtended wallItems) {
        mWallItems = wallItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mContainer;
        public TextView mName;
        public TextView mTime;
        public TextView mText;
        public RecyclerView mImages;
        public TextView mLikes;
        public TextView mComments;

        public ViewHolder(View itemView) {
            super(itemView);
            mContainer = (LinearLayout) itemView.findViewById(R.id.llWallContainer);
            mName = (TextView) itemView.findViewById(R.id.tvWallName);
            mTime = (TextView) itemView.findViewById(R.id.tvWallTime);
            mText = (TextView) itemView.findViewById(R.id.tvWallText);
            mImages = (RecyclerView) itemView.findViewById(R.id.rvWallImages);
            mComments = (TextView) itemView.findViewById(R.id.tvWallComments);
            mLikes = (TextView) itemView.findViewById(R.id.tvWallLikes);
        }

        public void clearAnimation() {
            mContainer.clearAnimation();
        }
    }

    @Override
    public WallAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wall, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WallAdapter.ViewHolder holder, int position) {
        if (mWallItems == null) return;

        VKApiPost post = mWallItems.wall.get(position);
        setPrimaryFields(holder, post);
        setAnimation(holder.mContainer, position);
    }

    @Override
    public int getItemCount() {
        return mWallItems.wall.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void updateAdapterData(VkWallExtended newItems) {
        int positionStart = mWallItems.wall.size();
        int itemCount = positionStart + newItems.wall.size();
        mWallItems.insert(newItems);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    private String getName(int userID) {
        String name = "";
        for(VKApiUser user : mWallItems.profile) {
            if (user.id == userID) {
                name = user.toString();
            }
        }
        return name;
    }

    private void setPrimaryFields(WallAdapter.ViewHolder holder, VKApiPost post) {
        holder.mName.setText(getName(post.from_id));
        Date date = new Date(post.date*1000);
        holder.mTime.setText(date.toString());
        holder.mText.setText(post.text);
        String comments = mContext.getResources().getString(R.string.comments)
                + " " + post.comments_count;
        holder.mComments.setText(comments);
        String likes = mContext.getResources().getString(R.string.likes)
                + " " + post.likes_count;
        holder.mLikes.setText(likes);
    }

    private void setAttachments(WallAdapter.ViewHolder holder, VKAttachments attachments) {
        Log.d(TAG, "setAttachments. Attachments size " + attachments.size());

        List<VKApiPhoto> photos = new ArrayList<>();
        for (VKAttachments.VKApiAttachment att : attachments) {
            String attachment = att.getType();
            if (attachment.equals(VKAttachments.TYPE_PHOTO)
                    || attachment.equals(VKAttachments.TYPE_POSTED_PHOTO)) {
                photos.add((VKApiPhoto) att);
            }
        }
        if (!photos.isEmpty()) {
            PostImagesAdapter adapter = new PostImagesAdapter(mContext, photos);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            holder.mImages.setLayoutManager(layoutManager);
            holder.mImages.setAdapter(adapter);
        }
    }
}
