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


    private String location; // TODO 이것만 남기거나 위도 경도만 남기거나 선택

    private String latitude;
    private String longitude;

    // TODO getter, setter 만들기


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

}
