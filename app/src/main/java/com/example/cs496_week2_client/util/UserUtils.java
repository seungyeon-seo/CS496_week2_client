package com.example.cs496_week2_client.util;

import android.content.Intent;

import com.example.cs496_week2_client.models.Post;
import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;

public class UserUtils {
    public static Intent getUserIntent(String userId, String nickname, ArrayList<String> posts, String token) {
        Intent intent = new Intent();
        // TODO response 객체로부터 id, nickname, posts 읽어오기
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

}
