package com.example.andrew.vkclient.util;

import com.example.andrew.vkclient.model.VkWallExtended;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WallExtendedResponseParser {

    private static final String TAG_RESPONSE = "response";
    private static final String TAG_COUNT = "count";
    private static final String TAG_POSTS = "items";
    private static final String TAG_PROFILES = "profiles";
    private static final String TAG_GROUPS = "groups";

    final public static VkWallExtended parse(String response) {
        VkWallExtended wallExtended = new VkWallExtended();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject responseObject = jsonObject.getJSONObject(TAG_RESPONSE);

            JSONArray posts = responseObject.getJSONArray(TAG_POSTS);
            JSONArray profiles = responseObject.getJSONArray(TAG_PROFILES);
            JSONArray communities = responseObject.getJSONArray(TAG_GROUPS);

            wallExtended.insert(
                    0,
                    parsePosts(posts),
                    parseProfiles(profiles),
                    parseCommunity(communities)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wallExtended;
    }

    private int parseCount(JSONObject item) throws JSONException {
        return item.getInt(TAG_COUNT);
    }

    private static List<VKApiPost> parsePosts(JSONArray items) {
        List<VKApiPost> posts = new ArrayList<>();
        try {
            for(int i = 0; i < items.length(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                posts.add(new VKApiPost(item));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private static List<VKApiUser> parseProfiles(JSONArray items) {
        List<VKApiUser> users = new ArrayList<>();
        try {
            for(int i = 0; i < items.length(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                users.add(new VKApiUser(item));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static List<VKApiCommunity> parseCommunity(JSONArray items) {
        List<VKApiCommunity> communities = new ArrayList<>();
        try {
            for(int i = 0; i < items.length(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                communities.add(new VKApiCommunity(item));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return communities;
    }

}
