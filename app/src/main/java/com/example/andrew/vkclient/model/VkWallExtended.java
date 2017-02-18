package com.example.andrew.vkclient.model;

import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;

import java.util.ArrayList;
import java.util.List;

public class VkWallExtended {
    public int count;
    public List<VKApiPost> wall = new ArrayList<>();
    public List<VKApiUser> profile = new ArrayList<>();
    public List<VKApiCommunity> group = new ArrayList<>();

    public VkWallExtended() {
    }

    public VkWallExtended insert(VkWallExtended newItem) {
        this.wall.addAll(newItem.wall);
        this.profile.addAll(newItem.profile);
        this.group.addAll(newItem.group);
        return this;
    }

    public void insert(int count, List<VKApiPost> wall, List<VKApiUser> profile, List<VKApiCommunity> group) {
        this.count = count;
        this.wall.addAll(wall);
        this.profile.addAll(profile);
        this.group.addAll(group);
    }
}
