package com.example.cs496_week2_client.util;

import android.content.Intent;
import android.os.Bundle;

import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;

public class UserUtils {
    public static Intent getUserIntent(User user, String token) {
        Intent intent = new Intent();
        intent.putExtra("token", token);
        intent.putExtra("nickName", user.getNickName());
        intent.putExtra("userId", user.getId());
        intent.putExtra("phoneNum", user.getPhoneNum());
        intent.putExtra("status", user.getStatus());
        intent.putExtra("profilePath", user.getProfilePath());
        intent.putExtra("groupCode", user.getGroupCode());
        intent.putStringArrayListExtra("contacts", user.getContacts());
        intent.putExtra("latitude", user.getLatitude());
        intent.putExtra("longitude", user.getLongitude());

        return intent;
    }

    public static User parseUserIntent(Intent intent) {
        User user = new User();

        user.setNickName(intent.getStringExtra("nickName"));
        user.setId(intent.getStringExtra("userId"));
        user.setPhoneNum(intent.getStringExtra("phoneNum"));
        user.setStatus(intent.getStringExtra("status"));
        user.setProfilePath(intent.getStringExtra("profilePath"));
        user.setGroupCode(intent.getStringExtra("groupCode"));
        user.setContacts(intent.getStringArrayListExtra("contacts"));
        user.setLatitude(intent.getStringExtra("latitude"));
        user.setLongitude(intent.getStringExtra("longitude"));

        return user;
    }

    public static String parseUserIntentGetToken(Intent intent) {
        return intent.getStringExtra("token");
    }


    public static Bundle getUserBundle(User user, String token) {
        Bundle bundle = new Bundle();
        bundle.putString("nickName", user.getNickName());
        bundle.putString("userId", user.getId());
        bundle.putString("phoneNum", user.getPhoneNum());
        bundle.putString("status", user.getStatus());
        bundle.putString("profilePath", user.getProfilePath());
        bundle.putString("groupCode", user.getGroupCode());
        bundle.putStringArrayList("contacts", user.getContacts());
        bundle.putString("latitude", user.getLatitude());
        bundle.putString("longitude", user.getLongitude());

        bundle.putString("token", token);
        return bundle;
    }

    public static User parseUserBundleGetUser(Bundle bundle) {
        User user = new User();
        user.setNickName(bundle.getString("nickName"));
        user.setId(bundle.getString("userId"));
        user.setPhoneNum(bundle.getString("phoneNum"));
        user.setStatus(bundle.getString("status"));
        user.setProfilePath(bundle.getString("profilePath"));
        user.setGroupCode(bundle.getString("groupCode"));
        user.setContacts(bundle.getStringArrayList("contacts"));
        user.setLatitude(bundle.getString("latitude"));
        user.setLongitude(bundle.getString("longitude"));

        return user;
    }

    public static String parseUserBundleGetToken(Bundle bundle) {
        return bundle.getString("token");
    }

    public static User getFakeUser() {
        User fakeUser = new User();

        ArrayList<String> fakeContacts = new ArrayList<>();
        fakeContacts.add("5ffc8ab317d84933795a33dc");
        fakeContacts.add("5ffc8ac517d84933795a33dd");

        // 제주도 시민 김땡땡 씨
        fakeUser.setNickName("김땡땡");
        fakeUser.setId("fake token");
        fakeUser.setPhoneNum("01012345678");
        fakeUser.setStatus("밥먹는중");
        fakeUser.setProfilePath("abc1610263375412.JPEG");
        fakeUser.setContacts(fakeContacts);
        fakeUser.setGroupCode("W5342");
        fakeUser.setLatitude("33.323749345895884");
        fakeUser.setLongitude("126.45983950440511");

        return fakeUser;
    }

}
