package com.example.cs496_week2_client.models;

import java.util.ArrayList;

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

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getStatus() {
        return status;
    }

    public String getNickName() {
        return nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
