package com.example.cs496_week2_client.models;

import com.example.cs496_week2_client.ui.contacts.Contact;

import java.lang.reflect.Array;
import java.util.ArrayList;

// TODO 서버의 models/group 에 맞춰 수정
public class User {
    private String nickName;
    private String id;
    private String phoneNum;
    private String status;
    private String profilePath;
    private String groupCode;
    private ArrayList<String> contacts;

    private String latitude;
    private String longitude;

    public String getNickName() {
        return nickName;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
