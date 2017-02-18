package com.example.andrew.vkclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrew.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {
    public static final String TAG = "DialogsAdapter";

    private VKList<VKApiDialog> mDialogs;
    private VKList<VKApiUserFull> mUsers;

    public DialogsAdapter(VKList<VKApiDialog> dialogs, VKList<VKApiUserFull> users) {
        mDialogs = dialogs;
        mUsers = users;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mAvatarImageView;
        public TextView mNameTextView;
        public TextView mLastMessageTextView;
        public ViewHolder(View itemView) {
            super(itemView);
//            mAvatarImageView = (ImageView) itemView.findViewById(R.id.dialog_avatar_image_view);
//            mNameTextView = (TextView) itemView.findViewById(R.id.dialog_name_text_view);
//            mLastMessageTextView = (TextView) itemView.findViewById(R.id.dialog_last_message_text_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialogs, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.mAvatarImageView.getContext();
        Picasso.with(context).load(mUsers.get(position).photo_100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.mAvatarImageView);
        holder.mNameTextView.setText(mUsers.get(position).toString());
        holder.mLastMessageTextView.setText(mDialogs.get(position).message.body);
    }

    @Override
    public int getItemCount() {
        return mDialogs.size();
    }
}
