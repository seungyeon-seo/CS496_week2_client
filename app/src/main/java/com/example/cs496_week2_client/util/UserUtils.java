package com.example.cs496_week2_client.util;

import android.content.Intent;
import android.os.Bundle;

import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;

// TODO 변경된 User 모델에 맞춰 메소드 손보기
public class UserUtils {
    public static Intent getUserIntent(User user) {
        Intent intent = new Intent();
        intent.putExtra("userId", user.getId());

        intent.putExtra("userId", user.getId());
        return intent;
    }

    public static User parseUserIntent(Intent intent) {
        String userId = intent.getStringExtra("userId");
        String nickname = intent.getStringExtra("nickname");
        ArrayList<String> posts = intent.getStringArrayListExtra("posts");

        User user = new User();
        user.setId(userId);
        user.setNickName(nickname);
        return user;
    }

    public static Bundle getUserBundle(User user, String token) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", user.getId());
        bundle.putString("nickname", user.getNickName());
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
        return user;
    }

    public static String parseUserBundleGetToken (Bundle bundle) {
       return bundle.getString("token");
    }

    public static User getFakeUser() {
        User fakeUser = new User();

        ArrayList<String> fakeContacts = new ArrayList<String>();
        fakeContacts.add("5ffc8ab317d84933795a33dc");
        fakeContacts.add("5ffc8ac517d84933795a33dd");

        fakeUser.setNickName("김땡땡");
        fakeUser.setId("id11111");
        fakeUser.setContacts(fakeContacts);
        fakeUser.setGroupCode("W5342");


        return fakeUser;
    }

}
