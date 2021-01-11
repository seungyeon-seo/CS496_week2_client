package com.example.cs496_week2_client.models;

import java.util.ArrayList;

// TODO 서버의 models/group 에 맞춰 수정
public class User {
    private String nickName;
    private String id;
    private ArrayList<String> posts;

    public ArrayList<String> getPosts() {
        return posts;
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

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }
}
