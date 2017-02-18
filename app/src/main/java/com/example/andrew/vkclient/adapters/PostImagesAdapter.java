package com.example.andrew.vkclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.andrew.vkclient.R;
import com.vk.sdk.api.model.VKApiPhoto;

import java.util.List;

public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.ViewHolder> {
    public static final String TAG = "PostImagesAdapter";

    private Context mContext;
    private List<VKApiPhoto> mImages;

    public PostImagesAdapter(Context mContext, List<VKApiPhoto> mImages) {
        this.mContext = mContext;
        this.mImages = mImages;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPostImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_images, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }
}
