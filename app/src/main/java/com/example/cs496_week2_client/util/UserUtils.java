package com.example.cs496_week2_client.util;

import android.content.Intent;
import android.os.Bundle;

import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;

public class UserUtils {
    public static Intent getUserIntent(String userId, String nickname, ArrayList<String> posts, String token) {
        Intent intent = new Intent();
        intent.putExtra("userId", userId);
        intent.putExtra("nickname", nickname);
        intent.putExtra("posts", posts);
        intent.putExtra("token", token);
        return intent;
    }

    public static User parseUserIntent(Intent intent) {
        String userId = intent.getStringExtra("userId");
        String nickname = intent.getStringExtra("nickname");
        ArrayList<String> posts = intent.getStringArrayListExtra("posts");

        User user = new User();
        user.setId(userId);
        user.setNickName(nickname);
        user.setPosts(posts);
        return user;
    }

    public static Bundle getUserBundle(User user, String token) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", user.getId());
        bundle.putString("nickname", user.getNickName());
        bundle.putStringArrayList("posts", user.getPosts());
        bundle.putString("token", token);
        return bundle;
    }

    public static User parseUserBundleGetUser(Bundle bundle) {
        String userId = bundle.getString("userId");
        String nickname = bundle.getString("nickname");
        ArrayList<String> posts = bundle.getStringArrayList("posts");

        User user = new User();
        user.setId(userId);
        user.setNickName(nickname);
        user.setPosts(posts);
        return user;
    }

    public static String parseUserBundleGetToken (Bundle bundle) {
       return bundle.getString("token");
    }

}
